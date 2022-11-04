package com.project.mp3;

import com.jfoenix.controls.JFXListView;
import com.project.mp3.components.Playlist;
import com.project.mp3.components.Song;
import com.project.mp3.events.SongEvent;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.VirtualFlow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.*;

public class MP3_Controller implements Initializable {

    @FXML
    private Label songLabel, currentTimeLabel, endTimeLabel;
    @FXML
    private Slider volumeSlider, songProgressSlider;
    @FXML
    private JFXListView<Song> songsListView;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ImageView playButtonImage, pauseButtonImage, restoreDownImage, maximizeImage;
    @FXML
    private VBox playlistsBox;
    @FXML
    private Pane TopPane;
    @FXML
    private Button maximizeButton, minimizeButton, restoreDownButton;


    private Media media;
    private MediaPlayer mediaPlayer;

    private int songIndex;
    private int selectedMusicInSongsListView;

    private Cell<Song> selectedCell;
    private Cell<Song> previousCell;

    public Song previousSong;
    public Song selectedSong;

    public static ArrayList<Playlist> playlists;
    public static int selectedPlaylist;

    private Timer timer;
    private double currentSongTime;
    private boolean playing;

    private double initialX;
    private double initialY;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File directory = new File("D:\\Work\\Univercity\\Term3\\CPP\\MP3\\src\\main\\resources\\com\\project\\mp3\\music");
        File[] files = directory.listFiles();

        playlists = new ArrayList<>();
        Playlist.setPlaylistsBox(playlistsBox);
        Playlist.setSongsListView(songsListView);

        // Add some test playlists
        playlists.add(new Playlist("Road To Home"));
        playlists.add(new Playlist("Evening"));

        playlists.get(0).addSong(new Song(files[0]));
        playlists.get(0).addSong(new Song(files[1]));
        playlists.get(0).addSong(new Song(files[0]));

        playlists.get(0).getSongByIndex(0).setStyle("-fx-text-fill: #34B743");
        playlists.get(0).getPlaylistButton().setStyle("-fx-text-fill: #34B743; -fx-font-weight: bold;");
        // End of test part

        changeSong(); // It also initializes mediaPlayer
        refreshSongsListView();

        // Set customs items in songListView
        songsListView.setCellFactory(new Callback<ListView<Song>, ListCell<Song>>() {
            @Override
            public ListCell<Song> call(ListView<Song> songListView) {
                final ListCell<Song> cell = new ListCell<Song>()
                {
                    @Override
                    public void updateItem(Song item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(playlists.get(selectedPlaylist).getAllSongs().indexOf(item)+1 + "   " + item.getName());
                            setStyle(item.getStyle());
                        }
                        else{
                            setText("");
                            setStyle("-fx-text-fill: white");
                        }
                    }
                };

                cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    if (event.getClickCount() == 2 && (! cell.isEmpty())) {
                       if (selectedCell != null){
                           previousCell = selectedCell;
                       }
                       else{
                           previousCell = getListCell(songsListView, songIndex);
                       }

                        assert previousCell != null;
                        previousCell.setStyle("-fx-text-fill: white");

                        selectedCell = cell;
                        selectedCell.setStyle("-fx-background-color: #414341; -fx-text-fill: #34B743");

                        if (previousSong != null)
                            previousSong.setStyle("-fx-text-fill: white");
                        selectedSong.setStyle("-fx-background-color: #414341; -fx-text-fill: #34B743");

                        songIndex = selectedMusicInSongsListView;
                        playNewSong();
                    }
                });

               cell.addEventFilter(SongEvent.SONG_CHANGED, songEvent -> {
                    previousSong.setStyle("-fx-text-fill: white");
                    selectedSong.setStyle("-fx-text-fill: #34B743");

                    refreshSongsListView();
               });

               return cell;
            }
        });

        addDraggableNode(TopPane);

        //Changes song volume
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }
        });

        // Changes music time after some actions
        songProgressSlider.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mediaPlayer.seek(Duration.seconds(songProgressSlider.getValue()));
                currentSongTime = songProgressSlider.getValue();
            }
        });
        songProgressSlider.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                mediaPlayer.seek(Duration.seconds(songProgressSlider.getValue()));
                currentSongTime = songProgressSlider.getValue();
            }
        });

        // Is used to set the value for progressSlider to run through whole song
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                Duration total = media.getDuration();
                songProgressSlider.setMax(total.toSeconds());
            }
        });

        // Makes songsListView fit size of scrollPane
        scrollPane.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                songsListView.setPrefHeight(t1.doubleValue());
            }
        });
        scrollPane.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                songsListView.setPrefWidth(t1.doubleValue());
            }
        });

        // Selects item in songsListView
        songsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object selectedSong) {
                var currentSong = (Song)selectedSong;
                selectedMusicInSongsListView = playlists.get(selectedPlaylist).getAllSongs().indexOf(currentSong);
            }
        });
    }

    private ListCell<Song> getListCell(JFXListView<Song> listView, int cellIndex) {
        if (cellIndex == -1) {
            return null;
        }
        //Virtual Flow is the container of all list cells
        //Each ListView has exactly one VirtualFlow which we are searching for
        Optional<VirtualFlow> virtualFlowOptional = listView.getChildrenUnmodifiable()
                .stream()
                .filter(node -> node instanceof VirtualFlow)
                .map(n -> (VirtualFlow) n)
                .findFirst();
        if (virtualFlowOptional.isEmpty()) {
            return null;
        }
        VirtualFlow<ListCell<Song>> virtualFlow = virtualFlowOptional.get();

        return virtualFlow.getCell(cellIndex);
    }

    public void createPlaylist(String name){ //// Make
        playlists.add(new Playlist(name));

    }

    public void addSong(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Music Files", "*.mp3")
        );

        var files = fileChooser.showOpenMultipleDialog(null);
        if (files != null){
            for (var item : files)
                playlists.get(selectedPlaylist).addSong(new Song(item));
        }
        refreshSongsListView();
    }

    public void removeSong(){
          playlists.get(selectedPlaylist).removeSongByIndex(selectedMusicInSongsListView);
          refreshSongsListView();
    }

    public void refreshSongsListView(){
        if (songsListView.getItems() == null)
            return;
        songsListView.getItems().clear();
        for (var song : playlists.get(selectedPlaylist).getAllSongs())
            songsListView.getItems().add(song);
    }

    private void playMedia(){
        beginTimer();

        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        mediaPlayer.seek(Duration.seconds(currentSongTime));

        mediaPlayer.play();
        playing = true;
        //Changes visibility of components
        pauseButtonImage.setVisible(true);
        playButtonImage.setVisible(false);
    }

    private void pauseMedia(){
        if (timer != null)
            cancelTimer();

        currentSongTime = mediaPlayer.getCurrentTime().toSeconds();
        mediaPlayer.pause();
        playing = false;
        //Changes visibility of components
        pauseButtonImage.setVisible(false);
        playButtonImage.setVisible(true);
    }

    private void stopMedia(){
        if (timer != null)
            cancelTimer();

        mediaPlayer.stop();
        playing = false;
        //Changes visibility of components
        pauseButtonImage.setVisible(false);
        playButtonImage.setVisible(true);
    }

    private void changeSong(){
        //Load song
        media = new Media(playlists.get(selectedPlaylist).getSongByIndex(songIndex).getURI());
        mediaPlayer = new MediaPlayer(media);
        //Change song label
        songLabel.setText(playlists.get(selectedPlaylist).getSongByIndex(songIndex).getName());

        //Is Used to set the value for progressSlider to run through whole song
        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                Duration total = media.getDuration();
                songProgressSlider.setMax(total.toSeconds());
            }
        });

        //Add listener to check changing of the time
        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration newValue) {
                displayCurrentTime();
                displayEndTime(media.getDuration().toSeconds());
                //If the song is finished than play new
                if (mediaPlayer.getCurrentTime().toSeconds() >= Math.floor(media.getDuration().toSeconds()))
                    nextMedia();
            }
        });

        // Changes selected song in songsListView
        songsListView.getSelectionModel().select(songIndex);

        // Mostly used to change the color of current playing song
        previousCell = selectedCell;
        if (previousCell == null)
            previousCell = getListCell(songsListView, songIndex-1);
        selectedCell = getListCell(songsListView, songIndex);

        previousSong = selectedSong;
        selectedSong = playlists.get(selectedPlaylist).getSongByIndex(songIndex);

        // Fire changed song event
        if (selectedCell != null)
            Event.fireEvent(selectedCell, new SongEvent(SongEvent.SONG_CHANGED));
    }

    private void displayCurrentTime(){
        int time = (int)mediaPlayer.getCurrentTime().toSeconds();
        int minutes = time/60;
        if (time-minutes*60 > 9)
            currentTimeLabel.setText(String.format("%x:%s", minutes, Integer.toString(time-minutes*60)));
        else
            currentTimeLabel.setText(String.format("%x:0%s", minutes, Integer.toString(time-minutes*60)));
    }

    private void displayEndTime(double seconds){
        int minutes = 0;
        while(seconds >= 60){
            seconds -= 60;
            minutes++;
        }
        if ((int)seconds > 9)
            endTimeLabel.setText(String.format("%x:%s", minutes, Integer.toString((int)seconds)));
        else
            endTimeLabel.setText(String.format("%x:0%s", minutes, Integer.toString((int)seconds)));
    }

    public void playNewSong(){
        stopMedia();
        changeSong();
        playMedia();
    }

    public void playMediaButton(){
        if (playing)
            pauseMedia();
        else
            playMedia();
    }

    public void previousMedia(){
        if (songIndex > 0){
            songIndex--;
            playNewSong();
        }

    }

    public void nextMedia(){
        if (playlists.get(selectedPlaylist).getSize()-1 > songIndex){
            songIndex++;
            playNewSong();
        }
    }

    private void beginTimer(){
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                double current = mediaPlayer.getCurrentTime().toSeconds();
                songProgressSlider.setValue(current);
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    private void cancelTimer(){
        timer.cancel();
    }

    private void addDraggableNode(final Node node) {
        node.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if (me.getButton() != MouseButton.MIDDLE) {
                    initialX = me.getSceneX();
                    initialY = me.getSceneY();
                }
            }
        });

        node.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if (me.getButton() != MouseButton.MIDDLE) {
                    node.getScene().getWindow().setX(me.getScreenX() - initialX);
                    node.getScene().getWindow().setY(me.getScreenY() - initialY);
                }
            }
        });
    }

    public void closeApp(ActionEvent event){
        Platform.exit();
        System.exit(0);
    }

    public void maximizeApp(ActionEvent event){
        Stage stage = (Stage) maximizeButton.getScene().getWindow();
        stage.setMaximized(true);

        restoreDownButton.setDisable(false);
        restoreDownButton.setVisible(true);
        restoreDownImage.setVisible(true);

        maximizeButton.setVisible(false);
        maximizeButton.setDisable(true);
        maximizeImage.setVisible(false);
    }

    public void minimizeApp(ActionEvent event){
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    public void restoreDownAction(ActionEvent event){
        Stage stage = (Stage) restoreDownButton.getScene().getWindow();
        stage.setMaximized(false);

        restoreDownButton.setDisable(true);
        restoreDownButton.setVisible(false);
        restoreDownImage.setVisible(false);

        maximizeButton.setVisible(true);
        maximizeButton.setDisable(false);
        maximizeImage.setVisible(true);
    }
}
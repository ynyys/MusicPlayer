package music;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Playlist {
    private final String playlistName;
    private ArrayList<Song> songs = new ArrayList<>();
    private int numOfSongs;

    public Playlist(String playlistName) {
        this.playlistName = playlistName;
    }

    public void playlistMenu() throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        Scanner scanner = new Scanner(System.in);
        String response = "";
        while (!response.equals("X")) {
            System.out.println(">>> " + playlistName + ". " + numOfSongs + " songs.\n");
            System.out.println("Playlist actions:");
            System.out.println("V - View all songs");
            System.out.println("A - Add Song");
            System.out.println("R - Remove Song");
            System.out.println("P - Play all songs");
            System.out.println("S - Shuffle and play all songs");
            System.out.println("X - Back to playlists menu");

            response = scanner.nextLine();
            response = response.toUpperCase();

            switch (response) {
                case ("V"):
                    viewAllSongs();
                    break;
                case ("A"):
                    try {
                        System.out.println("Enter song name to add:");
                        String songName = scanner.nextLine();
                        boolean songFound = false;

                        for (Song song : Application.getSongs()) {
                            if (songName.equalsIgnoreCase(song.getTitle())) {
                                songFound = true;
                                System.out.println("Do you want to add: " + song.displayInfo() + " to " + playlistName + "?\n(Y/N)");
                                String confirm = scanner.nextLine();
                                confirm = confirm.toUpperCase();

                                if (confirm.equals("Y")) {
                                    addSong(songName);

                                    PrintWriter fileWriter = new PrintWriter("playlists");
                                    for (Playlist playlist : Application.getPlaylists()) {

                                        String line = playlist.getPlaylistName() + " ";
                                        for (int i = 0; i < playlist.songs.size(); i++) {
                                            line += playlist.songs.get(i).getTitle().replaceAll(" ", "_") + ",";
                                        }
                                        fileWriter.println(line);
                                    }
                                    fileWriter.print("End.");
                                    fileWriter.close();
                                }
                                else {
                                    System.out.println(song.displayInfo() + ", will not be added to " + playlistName + ".");
                                }
                            }
                            if (songFound) {
                                break;
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Cannot find song to add, try again.");
                    }
                    break;
                case ("R"):
                    try {
                        System.out.println("Enter song name to remove:");
                        String songName = scanner.nextLine();
                        boolean songFound = false;

                        for (Song song : songs) {
                            if (songName.equalsIgnoreCase(song.getTitle())) {
                                songFound = true;
                                System.out.println("Do you want to remove: " + song.displayInfo() + " from " + playlistName + "?\n(Y/N)");
                                String confirm = scanner.nextLine();
                                confirm = confirm.toUpperCase();

                                if (confirm.equals("Y")) {
                                    removeSong(songName);

                                    PrintWriter fileWriter = new PrintWriter("playlists");
                                    for (Playlist playlist : Application.getPlaylists()) {

                                        String line = playlist.getPlaylistName() + " ";
                                        for (int i = 0; i < playlist.songs.size(); i++) {
                                            line += playlist.songs.get(i).getTitle().replaceAll(" ", "_") + ",";
                                        }
                                        fileWriter.println(line);
                                    }
                                    fileWriter.print("End.");
                                    fileWriter.close();
                                    break;
                                }
                                else {
                                    System.out.println(song.displayInfo() + ", will not be removed from " + playlistName + ".");
                                }
                            }
                        }
                        if (songFound) {
                            break;
                        }
                    } catch (Exception e) {
                        System.out.println("Cannot find song to remove, try again.");
                    }
                    break;
                case ("P"):
                    System.out.println("Now playing all songs in " + playlistName);
                    Player.playSequentially(songs);
                    break;
                case ("S"):
                    System.out.println("Now shuffling all songs in " + playlistName);
                    Player.shuffleSongs(songs);
                    break;
                case ("X"):
                    System.out.println("Exiting " + playlistName + "...");
                    break;
                default:
                    System.out.println("That is not a valid option.");
                    break;

            }
        }
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void addSong(String songName) {
        Song songToAdd = null;
        for (Song song : Application.getSongs()) {
            if (songName.equalsIgnoreCase(song.getTitle())) {
                songToAdd = song;
                break;
            }
        }

        boolean duplicate = false;
        for (Song song : songs) {
            if (songToAdd.equals(song)) {
                System.out.println("This song is already in this playlist.");
                duplicate = true;
                break;
            }
        }

        if (!duplicate) {
            System.out.println("Added " + songToAdd.displayInfo() + " to " + playlistName);
            songs.add(songToAdd);
            numOfSongs++;
        }
    }

    public void removeSong(String songName) {
        Song songToRemove;
        for (Song song : Application.getSongs()) {
            if (songName.equalsIgnoreCase(song.getTitle())) {
                songToRemove = song;
                System.out.println("Removed " + songToRemove.displayInfo() + " from " + playlistName);
                songs.remove(songToRemove);
                numOfSongs--;
                break;
            }
        }
    }

    public Song getSong(String songName) {
        for (Song song : songs) {
            if (song.getTitle().equals(songName)) {
                return song;
            }
        }
        System.out.println("Song not in playlist!");
        return null;
    }

    public String getSongsInPlaylist() {
        String songsInPlaylist = "";
        for (Song song : songs) {
            songsInPlaylist += song.getTitle() + ", by + " + song.getArtist() + "\n";
        }
        return songsInPlaylist;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
        this.numOfSongs = songs.size();
    }

    public String getPlaylistInfo() {
        return playlistName + " - " + numOfSongs + " in playlist";
    }

    public void viewAllSongs() {
        System.out.println("All songs in " + playlistName + ":");
        for (Song song : songs) {
            System.out.println(song.displayInfo());
        }
        System.out.println(" ");
    }
}

package music;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {
    private static ArrayList<Song> songs = new ArrayList<>();
    private static ArrayList<Playlist> playlists = new ArrayList<>();

    public static void init() {

        String response = "";
        Scanner scanner = new Scanner(System.in);
        while (!response.equals("x")) {
            System.out.println("> Main Menu\n");
            System.out.println("What would you like to do?");
            System.out.println("P - View Playlists");
            System.out.println("S - Search for song");
            System.out.println("X - Exit");

            response = scanner.nextLine();
            response = response.toUpperCase();

            switch (response) {
                case ("P"):
                    viewPlaylists();
                    break;
                case ("S"):
                    searchForSong();
                    break;
                case ("X"):
                    System.out.println("> Exiting application");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    public static void viewPlaylists() {
        Scanner scanner = new Scanner(System.in);
        String response = "";

        while (!response.equals("X")) {
            System.out.println(">> Playlists\n");
            System.out.println("Here are your current playlists:");
            for (Playlist playlist : playlists) {
                System.out.println(playlist.getPlaylistInfo());
            }

            System.out.println("\nWhat actions do you want to do?:");
            System.out.println("E - Explore a Playlist");
            System.out.println("S - Search for Song in Playlist");
            System.out.println("A - Add new Playlist");
            System.out.println("X - Back to menu");

            response = scanner.nextLine();
            response = response.toUpperCase();
            boolean found = false;

            switch (response) {
                case ("E"):
                    try {
                        System.out.println("What playlist do you want to explore?");
                        System.out.println("FYI: If playlist has spaces in name, use '_'. Otherwise, will not find.");
                        String playlistName = scanner.nextLine();

                        for (Playlist playlist : playlists) {
                            if (playlist.getPlaylistName().equalsIgnoreCase(playlistName)) {
                                playlist.playlistMenu();
                                found = true;
                            }
                        }
                        if (!found) {
                            System.out.println("Could not find playlist: '" + playlistName + "'");
                        }
                    } catch (Exception e) {
                        System.out.println("Please enter a valid playlist name.");
                    }
                    break;
                case ("X"):
                    System.out.println("Exiting playlists menu...");
                    break;
                default:
                    System.out.println("Invalid action");
                    break;
            }
        }
    }

    public static void searchForSong() {
        Scanner scanner = new Scanner(System.in);

        System.out.println(">> Search for song");
        System.out.println("What song are you looking for?");
        System.out.println("FYI: If song has spaces in name, use '_'. Otherwise, will not find.");

        String response = scanner.nextLine();
        response = response.toUpperCase();

        boolean found = false;

        try {
            for (Song song : songs) {
                if (song.getTitle().equalsIgnoreCase(response)) {
                    found = true;
                    System.out.println("Found " + song.displayInfo());
                    System.out.println("What would you like to do with this song?");
                    System.out.println("P - Play song");
                    System.out.println("A - Add song to a playlist");
                    System.out.println("X - Back to menu");

                    String songAction = scanner.nextLine();
                    songAction = songAction.toUpperCase();

                    switch (songAction) {
                        case ("P"):
                            Player.play(song);
                            break;
                        case ("A"):
                            System.out.println("What playlist do you want to add song to?");
                            String playlistName = scanner.nextLine();
                            playlistName = playlistName.toUpperCase();

                            boolean playlistFound = false;
                            for (Playlist playlist : playlists) {
                                if (playlist.getPlaylistName().equalsIgnoreCase(playlistName)) {
                                    playlistFound = true;
                                    System.out.println("Do you want to add: " + song.displayInfo() + " to " + playlist.getPlaylistName() + "?\n(Y/N)");
                                    String confirm = scanner.nextLine();
                                    confirm = confirm.toUpperCase();
                                    if (confirm.equals("Y")) {
                                        playlist.addSong(response);
                                        break;
                                    } else {
                                        System.out.println(song.displayInfo() + ", will not be added to " + playlist.getPlaylistName() + ".");
                                    }
                                }
                            }
                            if (!playlistFound) {
                                System.out.println("Could not find playlist: '" + playlistName + "'");
                            }
                            break;
                        case ("X"):
                            System.out.println("Exiting song-search...");
                            break;
                        default:
                            System.out.println("Invalid action");
                            break;
                    }
                }
            }
            if (!found) {
                System.out.println("Could not find '" + response + "'");
            }
        } catch (Exception e) {
            System.out.println("Please enter a valid song name.");
        }
    }

    public static void loadSongs() {
        try {
            Scanner scanner = new Scanner(new File("songs"));

            while (scanner.hasNextLine()) {
                String title = scanner.next();
                String artist = scanner.next();
                String fileName = scanner.next();

                Song song = new Song(title, artist, fileName);
                songs.add(song);
            }
            scanner.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Something went wrong, couldn't load songs.txt. Check directory.");
        }
    }

    public static void loadPlaylists() {
        try {
            Scanner scanner = new Scanner(new File("playlists"));

            while (scanner.hasNextLine()) {
                String playlistName = scanner.next();

                Playlist playlist = new Playlist(playlistName);
                String songs = scanner.next();
                ArrayList<Song> songList = populatePlaylist(songs);
                playlist.setSongs(songList);
                playlists.add(playlist);
            }
            scanner.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Something went wrong, couldn't load playlists.txt. Check directory.");
        }
    }

    private static ArrayList<Song> populatePlaylist(String fileSongs) {
        try {
            ArrayList<String> temp = new ArrayList<>();
            Scanner scanner = new Scanner(fileSongs);
            scanner.useDelimiter(",");

            while (scanner.hasNextLine()) {

                String currentLine = scanner.nextLine();

                //Split line apart using following delimiter:
                String[] songsToAdd = currentLine.split(",");

                for (String song : songsToAdd) {
                    temp.add(song);
                }

                ArrayList<Song> addSongs = new ArrayList<>();

                for (String song : temp) {
                    boolean found = false;
                    for (Song realSong : songs) {
                        if (song.equalsIgnoreCase(realSong.getTitle())) {
                            addSongs.add(realSong);
                            found = true;
                        }
                    }
                    if (!found) {
                        System.out.println("Could not find '" + song + "'. Check files for naming.");
                    }
                }
                return addSongs;
            }
            scanner.close();
        }
        catch (Exception e) {
            System.out.println("Something went wrong, couldn't populate playlists with songs. Check directory.");
        }
        return null;
    }

    public static ArrayList<Song> getSongs() {
        return songs;
    }
}

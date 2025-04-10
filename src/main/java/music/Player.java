package music;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Player {

    private static boolean stopAll = false;

    public static void play(Song song) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        stopAll = false;
        Scanner scanner = new Scanner(System.in);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(song.getSongFile());
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);

        //Calculating mins and secs for song length
        long songLength = clip.getMicrosecondLength() / 1000000;
        int mins = (int)(songLength / 60);
        int secs = (int)(songLength % 60);

        System.out.println("Song length - " + mins + " min, " + secs + " seconds.");
        clip.start();

        boolean songPlaying = true;
        System.out.println("Song actions: ");
        System.out.println("S - Start/Stop\nR - Reset\nN - Next\nX - Stop All");

        do {
            String response = scanner.nextLine();
            response = response.toUpperCase();

            switch (response) {
                case ("S"):
                    if (songPlaying) {
                        clip.stop();
                        songPlaying = false;
                        break;
                    } else if (!songPlaying) {
                        clip.start();
                        songPlaying = true;
                        break;
                    }
                case ("R"):
                    clip.setMicrosecondPosition(0);
                    break;
                case ("N"):
                    clip.setMicrosecondPosition(clip.getMicrosecondLength() + 10);
                    response = "";
                    break;
                case ("X"):
                    stopAll = true;
                    clip.setMicrosecondPosition(clip.getMicrosecondLength() + 10);
                    break;
                default:
                    System.out.println("Not a valid response");
                    break;
            }
        } while ((clip.getMicrosecondLength() > clip.getMicrosecondPosition()));
        clip.stop();
    }

    public static void playSequentially(ArrayList<Song> songs) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        for (Song song : songs) {
            System.out.println("Now playing: " +song.displayInfo());
            play(song);
            if (stopAll) {
                break;
            }
        }
    }

    public static void shuffleSongs(ArrayList<Song> songs) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        ArrayList<Song> songsToPlay = new ArrayList<>();
        songsToPlay.addAll(songs);

        Collections.shuffle(songsToPlay);

        for (Song song : songsToPlay) {
            System.out.println("Now playing " + song.displayInfo());
            play(song);
            if(stopAll) {
                break;
            }
        }
    }
}

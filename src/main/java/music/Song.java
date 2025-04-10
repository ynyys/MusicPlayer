package music;

import java.io.File;
import java.io.FileNotFoundException;

public class Song {
    private final String title;
    private final String artist;
    private final File songFile;

    public Song(String title, String artist, String filename) throws FileNotFoundException {
        this.title = title.replaceAll("_", " ");
        this.artist = artist.replaceAll("_", " ");
        songFile = new File(filename);
    }

    public String getTitle() {
        return title;
    }

    public File getSongFile() {
        return songFile;
    }

    public String getArtist() {
        return artist;
    }

    public String displayInfo() {
        String correctTitle = title;
        String correctArtist = artist;
        return correctTitle + ", by " + correctArtist;
    }
}

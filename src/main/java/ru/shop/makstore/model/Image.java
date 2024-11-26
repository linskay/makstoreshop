package ru.shop.makstore.model;

import jakarta.persistence.*;

import java.util.Arrays;
import java.util.Objects;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String filePath;
    private long fileSize;
    private String mediaType;
    @Lob
    private byte[] savesDataInDb;
    @OneToOne
    @JoinColumn(name = "electronic_cigarettes_id")
    private ElectronicCigarettes electronicCigarettes;
    @OneToOne
    @JoinColumn(name = "vape_id")
    private Vape vape;
    //    @OneToOne
//    @JoinColumn(name = "liquid id")
//    private Liquid liquid;
    @OneToOne
    @JoinColumn(name = "other_id")
    private Other other;

    public Image(){
    }

    public Image(int id, String filePath, long fileSize, String mediaType,
                 byte[] savesDataInDb, ElectronicCigarettes electronicCigarettes, Vape vape, Other other) {
        this.id = id;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.savesDataInDb = savesDataInDb;
        this.electronicCigarettes = electronicCigarettes;
        this.vape = vape;
        this.other = other;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getSavesDataInDb() {
        return savesDataInDb;
    }

    public void setSavesDataInDb(byte[] savesDataInDb) {
        this.savesDataInDb = savesDataInDb;
    }

    public ElectronicCigarettes getElectronicCigarettes() {
        return electronicCigarettes;
    }

    public void setElectronicCigarettes(ElectronicCigarettes electronicCigarettes) {
        this.electronicCigarettes = electronicCigarettes;
    }

    public Vape getVape() {
        return vape;
    }

    public void setVape(Vape vape) {
        this.vape = vape;
    }

    public Other getOther() {
        return other;
    }

    public void setOther(Other other) {
        this.other = other;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return id == image.id && fileSize == image.fileSize && Objects.equals(
                filePath, image.filePath) && Objects.equals(mediaType, image.mediaType)
                && Arrays.equals(savesDataInDb, image.savesDataInDb) && Objects.equals(
                electronicCigarettes, image.electronicCigarettes) && Objects.equals
                (vape, image.vape) && Objects.equals(other, image.other);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, filePath, fileSize, mediaType, electronicCigarettes, vape, other);
        result = 31 * result + Arrays.hashCode(savesDataInDb);
        return result;
    }
}

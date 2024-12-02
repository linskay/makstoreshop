package ru.shop.makstore.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Objects;

@Setter
@Getter
@Entity
public class Image{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String filePath;
    private long fileSize;
    private String mediaType;
    @Lob
    private byte[] savesDataInDb;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;


    public Image(){
    }

    public Image(int id, String filePath, long fileSize, String mediaType,
                 byte[] savesDataInDb, Product product) {
        this.id = id;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.savesDataInDb = savesDataInDb;
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image image = (Image) o;
        return id == image.id && fileSize == image.fileSize
                && Objects.equals(filePath, image.filePath)
                && Objects.equals(mediaType, image.mediaType)
                && Arrays.equals(savesDataInDb, image.savesDataInDb)
                && Objects.equals(product, image.product);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, filePath, fileSize, mediaType, product);
        result = 31 * result + Arrays.hashCode(savesDataInDb);
        return result;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", fileSize=" + fileSize +
                ", mediaType='" + mediaType + '\'' +
                ", savesDataInDb=" + Arrays.toString(savesDataInDb) +
                ", product=" + product +
                '}';
    }
}

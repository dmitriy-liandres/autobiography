package com.autobiography.model.db;

import javax.persistence.*;

/**
 * Author Dmitriy Liandres
 * Date 10.01.2016
 */
@Entity
@Table(name = "auto_bio_file")
@NamedQueries({
        @NamedQuery(
                name = "AutoBioFile.findByPersonId",
                query = "SELECT abf FROM AutoBioFile abf where abf.person.id=:personId"
        )
})
public class AutoBioFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "person_id")
    @ManyToOne()
    private Person person;

    @Column(name = "file_type", nullable = false, unique = false)
    private FileType fileType;

    @Column(name = "data_type", nullable = false, unique = false)
    private DataType dataType;

    @Column(name = "file_name", nullable = false, unique = false)
    private String filename;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }
}

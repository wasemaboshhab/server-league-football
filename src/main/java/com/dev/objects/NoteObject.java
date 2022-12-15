package com.dev.objects;

import javax.persistence.*;

@Entity
@Table(name = "notes")
public class NoteObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    int id;

    @Column
    private String text;

    @Column(name = "some_other_name")
    private String otherText;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private NoteObject noteObject;

    public String getOtherText() {
        return otherText;
    }

    public void setOtherText(String otherText) {
        this.otherText = otherText;
    }

    public NoteObject getNoteObject() {
        return noteObject;
    }

    public void setNoteObject(NoteObject noteObject) {
        this.noteObject = noteObject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

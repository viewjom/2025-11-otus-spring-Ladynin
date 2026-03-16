package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.DBRef;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.bson.Document;

@Getter
@Setter
@ChangeLog
public class DatabaseChangelog {

    private Document bookDoc1;

    private Document bookDoc2;

    private Document bookDoc3;

    @ChangeSet(order = "001", id = "dropTables", author = "ladynin", runAlways = true)
    public void dropTables(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "addBooks", author = "ladynin")
    public void addBooks(MongoDatabase db) {

        MongoCollection<Document> authorsCollection = db.getCollection("authors");
        Document authorDoc1 = new Document().append("fullName", "Author_1");
        Document authorDoc2 = new Document().append("fullName", "Author_2");
        Document authorDoc3 = new Document().append("fullName", "Author_3");
        authorsCollection.insertMany(List.of(authorDoc1, authorDoc2, authorDoc3));

        //Genres
        MongoCollection<Document> genresCollection = db.getCollection("genres");
        var genreDoc1 = new Document().append("name", "Genre_1");
        var genreDoc2 = new Document().append("name", "Genre_2");
        var genreDoc3 = new Document().append("name", "Genre_3");
        genresCollection.insertMany(List.of(genreDoc1, genreDoc2, genreDoc3));

        //Books
        MongoCollection<Document> booksCollection = db.getCollection("books");
        bookDoc1 = new Document().append("title", "BookTitle_1").append("author", authorDoc1)
                .append("genre", genreDoc1);
        bookDoc2 = new Document().append("title", "BookTitle_2").append("author", authorDoc2)
                .append("genre", genreDoc2);
        bookDoc3 = new Document().append("title", "BookTitle_3").append("author", authorDoc3)
                .append("genre", genreDoc3);
        booksCollection.insertMany(List.of(bookDoc1, bookDoc2, bookDoc3));
    }

    @ChangeSet(order = "003", id = "addComments", author = "ladynin")
    public void addComments(MongoDatabase db) {

        DBRef addressRef1 = new DBRef("books", bookDoc1.getObjectId("_id"));
        DBRef addressRef2 = new DBRef("books", bookDoc2.getObjectId("_id"));
        DBRef addressRef3 = new DBRef("books", bookDoc3.getObjectId("_id"));

        MongoCollection<Document> commentsCollection = db.getCollection("comments");

        var commentDoc1 = new Document().append("text", "Хорошая книга").append("book", addressRef1);
        var commentDoc2 = new Document().append("text", "Очень хорошая книга").append("book", addressRef1);
        var commentDoc3 = new Document().append("text", "Нормальная книга").append("book", addressRef2);
        var commentDoc4 = new Document().append("text", "Прекрасная книга").append("book", addressRef2);
        var commentDoc5 = new Document().append("text", "Великолепная книга").append("book", addressRef3);
        var commentDoc6 = new Document().append("text", "Крутая книга").append("book", addressRef3);

        commentsCollection.insertMany(List.of(commentDoc1, commentDoc2, commentDoc3, commentDoc4, commentDoc5
                , commentDoc6));
    }
}
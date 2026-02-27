package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.List;
import org.bson.Document;

@ChangeLog
public class DatabaseChangelog {
    @ChangeSet(order = "001", id = "dropTables", author = "ladynin", runAlways = true)
    public void dropTables(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "addBooks", author = "ladynin")
    public void addBooks(MongoDatabase db) {

        MongoCollection<Document> authorsCollection = db.getCollection("authors");
        var authorDoc1 = new Document("_id", "1").append("fullName", "Author_1");
        var authorDoc2 = new Document("_id", "2").append("fullName", "Author_2");
        var authorDoc3 = new Document("_id", "3").append("fullName", "Author_3");
        authorsCollection.insertMany(List.of(authorDoc1, authorDoc2, authorDoc3));

        //Genres
        MongoCollection<Document> genresCollection = db.getCollection("genres");
        var genreDoc1 = new Document("_id", "1").append("name", "Genre_1");
        var genreDoc2 = new Document("_id", "2").append("name", "Genre_2");
        var genreDoc3 = new Document("_id", "3").append("name", "Genre_3");
        genresCollection.insertMany(List.of(genreDoc1, genreDoc2, genreDoc3));

        //Books
        MongoCollection<Document> booksCollection = db.getCollection("books");
        var bookDoc1 = new Document("_id", "1")
                .append("title", "BookTitle_1")
                .append("author", authorDoc1)
                .append("genre", genreDoc1);
        var bookDoc2 = new Document("_id", "2")
                .append("title", "BookTitle_2")
                .append("author", authorDoc2)
                .append("genre", genreDoc2);
        var bookDoc3 = new Document("_id", "3")
                .append("title", "BookTitle_3")
                .append("author", authorDoc3)
                .append("genre", genreDoc3);
        booksCollection.insertMany(List.of(bookDoc1, bookDoc2, bookDoc3));
    }

    @ChangeSet(order = "003", id = "addComments", author = "ladynin")
    public void addComments(MongoDatabase db) {

        MongoCollection<Document> booksCollection = db.getCollection("books");
        Document bookDoc1 = booksCollection.find(new Document("_id", "1")).first();

        MongoCollection<Document> commentsCollection = db.getCollection("comments");

        var commentDoc1 = new Document("_id", "1")
                .append("text", "Хорошая книга")
                .append("book", bookDoc1);
        var commentDoc2 = new Document("_id", "2")
                .append("text", "Очень хорошая книга")
                .append("book", bookDoc1);
/*
        var commentDoc3 = new Document("id", "3")
                .append("text", "Нормальная книга")
                .append("book", bookDoc2);
        var commentDoc4 = new Document("id", "4")
                .append("text", "Прекрасная книга")
                .append("book", bookDoc2);

        var commentDoc5 = new Document("id", "5")
                .append("text", "Великолепная книга")
                .append("book", bookDoc3);
        var commentDoc6 = new Document("id", "6")
                .append("text", "Классная книга")
                .append("book", bookDoc3);
        commentsCollection.insertMany(List.of(commentDoc1, commentDoc2, commentDoc3,
                commentDoc4, commentDoc5, commentDoc6));

         */
        commentsCollection.insertMany(List.of(commentDoc1, commentDoc2));
    }

}
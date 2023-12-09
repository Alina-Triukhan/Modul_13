package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class TaskModule_12 {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) throws IOException {
//        createUser();
//        updatingUserById(5);
//        removeUserByID(3);
//        infoAboutUsers();
//        findUserById(1);
//        findUserByUsername("Antonette");
//        lastUser_sCommentsToLastPost(7);
//        lastUser_sCommentsToLastPost(11);
        haveToDo(2);
    }

    private static void createUser() throws IOException {
        String url = "https://jsonplaceholder.typicode.com/users";
        Document doc_users = Jsoup.connect(url).ignoreContentType(true).get();
        User[] users = gson.fromJson(doc_users.text(), User[].class);
        int maxId = Arrays.stream(users).mapToInt(User::getId).max().orElse(0);

        User newUser = new User(maxId + 1, "Angelina Jolie", "Voight", "https://www.instagram.com/angelinajolie/",
                new Address("Palisades Village", "887 Toyopa Dr", "Los Angeles", "468207-492 f547",
                        new Geo(34.0448544, -118.537686)), "+1 310-5251380", "https://angelina-jolie.com/",
                new Company("Maleficent", "Humanitarian activities", "goodwill ambassador"));

        System.out.println("Нового користувача створено успішно! \n" + gson.toJson(newUser));
    }

    private static void updatingUserById(int id) throws IOException {
        String url = "https://jsonplaceholder.typicode.com/users/" + id;
        Connection.Response response = Jsoup.connect(url)
                .ignoreContentType(true)
                .method(Connection.Method.PUT)
                .execute();
        User oldDataUser = gson.fromJson(Jsoup.connect(url).ignoreContentType(true).get().text(), User.class);

        User updatingUser = new User(id, "Angelina Jolie", "Voight", "https://www.instagram.com/angelinajolie/",
                new Address("Palisades Village", "887 Toyopa Dr", "Los Angeles", "468207-492 f547",
                        new Geo(34.0448544, -118.537686)), "+1 310-5251380", "https://angelina-jolie.com/",
                new Company("Maleficent", "Humanitarian activities", "goodwill ambassador"));

        System.out.println("Данні користувача з id " + id + "\n" + gson.toJson(oldDataUser) + "\n було успішно оновлено: \n" + gson.toJson(updatingUser));
        System.out.println("response.statusCode() = " + response.statusCode());
    }

    private static void removeUserByID(int id) throws IOException {
        String url = "https://jsonplaceholder.typicode.com/users/" + id;
        Connection.Response response = Jsoup.connect(url)
                .ignoreContentType(true)
                .method(Connection.Method.DELETE)
                .execute();
        if (response.statusCode() == 200) {
            System.out.println("Користувач з id " + id + " видалено успішно!");
            System.out.println("response.statusCode() = " + response.statusCode());
        } else {
            System.out.println("Помилка при видаленні користувача. response.statusCode() = " + response.statusCode());
        }
    }

    private static void infoAboutUsers() throws IOException {
        String url = "https://jsonplaceholder.typicode.com/users";
        Document doc_users = Jsoup.connect(url).ignoreContentType(true).get();
        User[] users = gson.fromJson(doc_users.text(), User[].class);
        System.out.println("Список користувачів:\n" + gson.toJson(users));
    }

    private static void findUserById(int id) throws IOException {
        String url = "https://jsonplaceholder.typicode.com/users/" + id;
        Document doc_users = Jsoup.connect(url).ignoreContentType(true).get();
        User user = gson.fromJson(doc_users.text(), User.class);
        System.out.println("Користувач з id " + id + ":\n" + gson.toJson(user));
    }

    private static void findUserByUsername(String username) throws IOException {
        String url = "https://jsonplaceholder.typicode.com/users?username=" + username;
        Document doc_users = Jsoup.connect(url).ignoreContentType(true).get();
        User[] users = gson.fromJson(doc_users.text(), User[].class); //на той випадок, якщо осіб з таким ім'ям користувача декілька
        System.out.println("Користувач " + username + ":\n" + gson.toJson(users));
    }

    private static void lastUser_sCommentsToLastPost(int id) throws IOException {
        String url = "https://jsonplaceholder.typicode.com/users/" + id + "/posts";
        Document doc_userPosts = Jsoup.connect(url).ignoreContentType(true).get();
        Post[] posts = gson.fromJson(doc_userPosts.text(), Post[].class);

        if (posts.length == 0) {
            System.out.println("У користувача немає постів.");
            return;
        }
        Post lastPost = Arrays.stream(posts).max(Comparator.comparingInt(Post::getId)).orElse(null);

        String url1 = "https://jsonplaceholder.typicode.com/posts/" + lastPost.getId() + "/comments";
        Document doc_postComments = Jsoup.connect(url1).ignoreContentType(true).get();
        Comment[] comments = gson.fromJson(doc_postComments.text(), Comment[].class);

        if (comments.length > 0) {
            String file = "user-" + id + "-post-" + lastPost.getId() + "-comments.json";
            try (FileWriter fw = new FileWriter(file)) {
                gson.toJson(comments, fw);
                System.out.println(gson.toJson(comments));
                System.out.println("Коментарі до останнього посту користувача збережені у файл: " + file);
            } catch (IOException e) {
                System.out.println("Помилка при записі коментарів у файл.");
                e.printStackTrace();
            }
        }
    }

    private static void  haveToDo(int id) throws IOException {
        String url = "https://jsonplaceholder.typicode.com/users/" + id + "/todos";
        Document doc_todos = Jsoup.connect(url).ignoreContentType(true).get();
        Todo[] userTodos = gson.fromJson(doc_todos.text(), Todo[].class);

        if (userTodos.length > 0) {
            System.out.println("Відкриті задачі для користувача з id " + id + ":\n");
            Arrays.stream(userTodos)
                    .filter(todo -> !todo.isCompleted())
                    .forEach(todo -> System.out.println(gson.toJson(todo)));
        } else {
            System.out.println("У користувача немає не виконаних задач.");
        }
    }
}

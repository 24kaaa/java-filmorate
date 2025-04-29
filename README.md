# java-filmorate
Template repository for Filmorate project.
[Диаграмма базы данных](shema.png)

users
-
user_id pk int
email varchar(50)
login varchar(50)
birthday date

films
-
film_id pk int
name varchar(50)
description varchar(200)
release_date date
duration int
rating_id int FK >- ratings.rating_id

film_genres
-
film_id pk int FK >- films.film_id
genre_id int FK >- genres.genre_id


genres
-
genre_id pk int
name varchar(50)

ratings
-
rating_id pk int
name varchar(50)

likes
-
user_id int FK >- users.user_id
film_id int FK >- films.film_id

friends
-
user_id FK >- users.user_id
friend_id FK >- users.user_id

CREATE TABLE IF NOT EXISTS public.comments
(
    id bigserial NOT NULL,
    post_id integer NOT NULL,
    comment text COLLATE pg_catalog."default",
    CONSTRAINT comments_pkey PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS public.likes
(
    id serial NOT NULL,
    post_id integer NOT NULL,
    likes integer,
    CONSTRAINT likes_pkey PRIMARY KEY (id)
    );

CREATE TABLE IF NOT EXISTS public.posts
(
    id bigserial NOT NULL,
    title text COLLATE pg_catalog."default" NOT NULL,
    text text COLLATE pg_catalog."default",
    img_url text COLLATE pg_catalog."default",
    tags text COLLATE pg_catalog."default",
    CONSTRAINT posts_pkey PRIMARY KEY (id)
    );

ALTER TABLE IF EXISTS public.comments
    ADD CONSTRAINT post_id FOREIGN KEY (post_id)
    REFERENCES public.posts (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public.likes
    ADD CONSTRAINT post_id FOREIGN KEY (post_id)
    REFERENCES public.posts (id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION
    NOT VALID;

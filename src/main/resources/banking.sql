--
-- PostgreSQL database dump
--

-- Dumped from database version 15.5
-- Dumped by pg_dump version 15.5

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: user_balance; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_balance (
    balance numeric(38,2) NOT NULL,
    id bigint NOT NULL
);


ALTER TABLE public.user_balance OWNER TO postgres;

--
-- Name: user_balance_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_balance_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_balance_id_seq OWNER TO postgres;

--
-- Name: user_balance_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_balance_id_seq OWNED BY public.user_balance.id;


--
-- Name: user_operation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_operation (
    id integer NOT NULL,
    created_date timestamp with time zone NOT NULL,
    user_id bigint NOT NULL,
    operation character varying NOT NULL,
    amount_of_money numeric(19,2) NOT NULL
);


ALTER TABLE public.user_operation OWNER TO postgres;

--
-- Name: user_operation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_operation_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_operation_id_seq OWNER TO postgres;

--
-- Name: user_operation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_operation_id_seq OWNED BY public.user_operation.id;


--
-- Name: user_balance id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_balance ALTER COLUMN id SET DEFAULT nextval('public.user_balance_id_seq'::regclass);


--
-- Name: user_operation id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_operation ALTER COLUMN id SET DEFAULT nextval('public.user_operation_id_seq'::regclass);


--
-- Data for Name: user_balance; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_balance (balance, id) FROM stdin;
3003333.22	12
97.22	122
198.13	1
\.


--
-- Data for Name: user_operation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_operation (id, created_date, user_id, operation, amount_of_money) FROM stdin;
1	2023-11-27 00:11:35.317609+03	122	TAKE	1.00
2	2023-11-27 00:12:34.681665+03	122	TAKE	1.00
3	2023-11-27 00:12:47.798109+03	1	TAKE	1.00
4	2023-11-27 00:13:13.004375+03	1	TAKE	2.00
5	2023-11-27 00:14:44.206633+03	1	ADD	101.00
\.


--
-- Name: user_balance_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_balance_id_seq', 1, false);


--
-- Name: user_operation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_operation_id_seq', 5, true);


--
-- Name: user_balance user_balance_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_balance
    ADD CONSTRAINT user_balance_pkey PRIMARY KEY (id);


--
-- Name: user_operation user_operation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_operation
    ADD CONSTRAINT user_operation_pkey PRIMARY KEY (id);


--
-- Name: user_operation user_operation_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_operation
    ADD CONSTRAINT user_operation_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.user_balance(id);


--
-- PostgreSQL database dump complete
--


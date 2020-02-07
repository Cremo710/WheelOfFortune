

CREATE TABLE public.frase (
    idfrase bigint NOT NULL,
    tema character varying(60) NOT NULL,
    corpo character varying(60) NOT NULL
);


ALTER TABLE public.frase OWNER TO postgres;


CREATE SEQUENCE public.frase_idfrase_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.frase_idfrase_seq OWNER TO postgres;


ALTER SEQUENCE public.frase_idfrase_seq OWNED BY public.frase.idfrase;


CREATE SEQUENCE public.id_mossa_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.id_mossa_seq OWNER TO postgres;


CREATE TABLE public.manche (
    idmanche bigint NOT NULL,
    idpartita bigint NOT NULL,
    idfrase bigint,
    punteggio integer,
    vincitore character varying(50)
);


ALTER TABLE public.manche OWNER TO postgres;


CREATE TABLE public.mossa (
    idmossa bigint DEFAULT nextval('public.id_mossa_seq'::regclass) NOT NULL,
    idmanche bigint NOT NULL,
    idutente character varying(50) NOT NULL,
    idpartita bigint NOT NULL,
    punti integer,
    nome character varying(5)
);


ALTER TABLE public.mossa OWNER TO postgres;


CREATE TABLE public.partecipazione (
    email character varying(50) NOT NULL,
    idpartita bigint NOT NULL,
    tipo integer
);


ALTER TABLE public.partecipazione OWNER TO postgres;


CREATE TABLE public.partita (
    idpartita integer NOT NULL,
    datainizio bigint NOT NULL,
    datafine bigint NOT NULL,
    email character varying(50) NOT NULL,
    nmanche integer,
    winner character varying(50),
    turno integer
);


ALTER TABLE public.partita OWNER TO postgres;


CREATE SEQUENCE public.partita_idpartita_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.partita_idpartita_seq OWNER TO postgres;


ALTER SEQUENCE public.partita_idpartita_seq OWNED BY public.partita.idpartita;


CREATE TABLE public.utente (
    email character varying(50) NOT NULL,
    nome character varying(50) NOT NULL,
    cognome character varying(50) NOT NULL,
    nickname character varying(10) NOT NULL,
    password character varying(100) NOT NULL,
    admin boolean NOT NULL,
    oracodice bigint,
    online boolean
);


ALTER TABLE public.utente OWNER TO postgres;

ALTER TABLE ONLY public.frase ALTER COLUMN idfrase SET DEFAULT nextval('public.frase_idfrase_seq'::regclass);



ALTER TABLE ONLY public.partita ALTER COLUMN idpartita SET DEFAULT nextval('public.partita_idpartita_seq'::regclass);




ALTER TABLE ONLY public.mossa
    ADD CONSTRAINT constraintmossa UNIQUE (idmossa, idmanche, idutente, idpartita);



ALTER TABLE ONLY public.manche
    ADD CONSTRAINT constraintnameunique UNIQUE (idmanche, idpartita);


ALTER TABLE ONLY public.frase
    ADD CONSTRAINT frase_pkey PRIMARY KEY (idfrase);


ALTER TABLE ONLY public.manche
    ADD CONSTRAINT manche_pkey PRIMARY KEY (idmanche, idpartita);


ALTER TABLE ONLY public.mossa
    ADD CONSTRAINT mossa_pkey PRIMARY KEY (idmossa, idmanche, idutente, idpartita);


ALTER TABLE ONLY public.utente
    ADD CONSTRAINT nickname UNIQUE (nickname);


ALTER TABLE ONLY public.partecipazione
    ADD CONSTRAINT partecipazione_pkey PRIMARY KEY (email, idpartita);


--
-- TOC entry 2735 (class 2606 OID 16560)
-- Name: partita partita_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.partita
    ADD CONSTRAINT partita_pkey PRIMARY KEY (idpartita);


ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (email);


CREATE INDEX fki_email ON public.partita USING btree (email);


CREATE INDEX fki_ma_idfrase_fkey ON public.manche USING btree (idfrase);


CREATE INDEX fki_ma_idpartita_fkey ON public.manche USING btree (idpartita);


CREATE INDEX fki_mo_idmanche_fkey ON public.mossa USING btree (idmanche, idpartita);


CREATE INDEX fki_p_email ON public.partecipazione USING btree (email);


CREATE INDEX fki_pa_idpartita ON public.partecipazione USING btree (idpartita);


ALTER TABLE ONLY public.manche
    ADD CONSTRAINT ma_idfrase_fkey FOREIGN KEY (idfrase) REFERENCES public.frase(idfrase) ON UPDATE SET NULL ON DELETE SET NULL;


COMMENT ON CONSTRAINT ma_idfrase_fkey ON public.manche IS 'chiave esterna sulla tabella frase, in caso di eliminazione della frase, il campo verrà settato a null';


ALTER TABLE ONLY public.manche
    ADD CONSTRAINT ma_idpartita_fkey FOREIGN KEY (idpartita) REFERENCES public.partita(idpartita) ON UPDATE CASCADE ON DELETE CASCADE;


COMMENT ON CONSTRAINT ma_idpartita_fkey ON public.manche IS 'chiave esterna sulla tabella partita, indica a che partita si riferisce la manche, in caso di eliminazione della partita , le manche verranno eliminate';


ALTER TABLE ONLY public.mossa
    ADD CONSTRAINT mo_idmanche_fkey FOREIGN KEY (idmanche, idpartita) REFERENCES public.manche(idmanche, idpartita) ON UPDATE CASCADE ON DELETE CASCADE;


COMMENT ON CONSTRAINT mo_idmanche_fkey ON public.mossa IS 'chiave esterna sulla tabella manche, in caso di eliminazione della manche, la mossa verrà eliminata';


ALTER TABLE ONLY public.mossa
    ADD CONSTRAINT mo_idutente_fkey FOREIGN KEY (idutente) REFERENCES public.utente(email) ON UPDATE CASCADE ON DELETE CASCADE;


COMMENT ON CONSTRAINT mo_idutente_fkey ON public.mossa IS 'chiave esterna sulla tabella utente , se un utente viene eliminato la mossa rispettiva sarà eliminata';


ALTER TABLE ONLY public.partecipazione
    ADD CONSTRAINT pa_email_fkey FOREIGN KEY (email) REFERENCES public.utente(email) ON UPDATE CASCADE ON DELETE CASCADE;



COMMENT ON CONSTRAINT pa_email_fkey ON public.partecipazione IS 'chiave esterna sulla tabella utente, nel caso di eliminazione di un utente la partecipazione alla partita sarà eliminata';



ALTER TABLE ONLY public.partecipazione
    ADD CONSTRAINT pa_idpartita_fkey FOREIGN KEY (idpartita) REFERENCES public.partita(idpartita) ON UPDATE CASCADE ON DELETE CASCADE;



COMMENT ON CONSTRAINT pa_idpartita_fkey ON public.partecipazione IS 'chiave esterna sulla tabella partita, in caso di eliminazione della partita , la partecipazione verrà eliminata.';



ALTER TABLE ONLY public.partita
    ADD CONSTRAINT pt_email_fkey FOREIGN KEY (email) REFERENCES public.utente(email) ON UPDATE SET NULL ON DELETE SET NULL;



COMMENT ON CONSTRAINT pt_email_fkey ON public.partita IS 'chiave esterna su utente , nel caso di eliminazione di un utente l''attributo email che indica chi ha creato la partita verrà settato a NULL';



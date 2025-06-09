package com.example.cinemadridapp.Objetos;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class Pelicula {

    private String nombre;
    private String director;
    private int año;
    private double duracion;
    private String generos;
    private String descripcion;
    private String poster;
    private double notaGlobal;

    public static HashMap<String , Integer> mapaIdPosters = new HashMap<>();

    public Pelicula(String nombre, String director, int año, double duracion, String generos, String descripcion, String poster, double notaGlobal) {
        this.nombre = nombre;
        this.director = director;
        this.año = año;
        this.duracion = duracion;
        this.generos = generos;
        this.descripcion = descripcion;
        this.poster = poster;
        this.notaGlobal = notaGlobal;
    }

    public static ArrayList<Pelicula> peliculas = new ArrayList<>();

    static {
        Pelicula pJaws = new Pelicula("Jaws", "Steven Spielberg", 1975, 124.0, "Aventura, Drama, Thriller",
                "Cuando un tiburón asesino desata el caos en una comunidad playera de Long Island, el sheriff local, un biólogo marino y un viejo marinero deben dar caza a la bestia.", "poster_jaws", 8.1);

        Pelicula pPulpFiction = new Pelicula("Pulp Fiction", "Quentin Tarantino", 1994, 154.0, "Crimen, Gangster, Drama",
                "Crimen, Gangster, Drama', 'Las vidas de dos mafiosos, un boxeador, la esposa de un gánster y un par de bandidos se entrelazan en cuatro historias de violencia y redención.", "poster_pulp_fiction", 8.9);

        Pelicula pScarface = new Pelicula("Scarface", "Brian De Palma", 1983, 169.0, "Gangster, Crimen, Drama",
                "En el Miami de 1980, un decidido inmigrante cubano se hace con un cártel de la droga y sucumbe a la codicia.", "poster_scarface", 8.3);

        Pelicula pMadMax = new Pelicula("Mad Max", "George Miller", 2015, 120.0, "Accion, Sci-Fi, Aventura",
                "En un páramo postapocalíptico, una mujer se rebela contra un gobernante tiránico en busca de su patria con la ayuda de un grupo de prisioneras, un adorador psicótico y un vagabundo llamado Max.", "poster_mad_max", 8.2);


        Pelicula pInterstellar = new Pelicula("Interstellar", "Christopher Nolan", 2014 , 169.0, "Sci-Fi, Aventura, Drama",
                "Un equipo de exploradores viaja a través de un agujero de gusano en el espacio en un intento de garantizar la supervivencia de la humanidad.", "poster_interstellar", 8.7);

        Pelicula pAlien = new Pelicula("Alien", "Ridley Scott", 1979, 117.0, "Terror, Sci-Fi",
                "La tripulación de una nave espacial comercial se encuentra con una forma de vida mortal tras investigar una transmisión desconocida.", "poster_alien", 8.5);

        Pelicula pGladiator = new Pelicula("Gladiator", "Ridley Scott", 2001, 155.0, "Historia, Aventura, Drama",
                "Un exgeneral romano se propone vengarse del emperador corrupto que asesinó a su familia y lo envió a la esclavitud.", "poster_gladiator", 8.5);

        Pelicula pRTOK = new Pelicula("El Retorno del Rey", "Peter Jackson", 2001, 169.0, "Fantasia, Aventura, Accion",
                "Gandalf y Aragorn lideran el mundo de los hombres contra la armada de Sauron para distraer su atención de Frodo y Sam, quienes se aproximan al Monte del Destino con el Anillo Único.", "poster_rotk", 9.0);

        Pelicula pHeat = new Pelicula("Heat", "Michael Mann", 1995, 170.0, "Accion, Crimen, Drama",
                "Un grupo de ladrones profesionales de alto nivel empieza a sentir a la policía de Los Ángeles en los talones cuando, sin saberlo, deja una pista en su último atraco.", "poster_heat", 8.3);




        Pelicula pDune2021 = new Pelicula("Dune", "Denis Villeneuve", 2021, 155.0, "Sci-Fi, Aventura, Drama",
                "Una familia noble se ve envuelta en una guerra por el control del bien más valioso de la galaxia mientras su heredero se ve perturbado por visiones de un futuro oscuro.", "poster_dune_part_one", 8.0);

        Pelicula pDune2 = new Pelicula("Dune Parte Dos", "Denis Villeneuve", 2024, 166.0, "Sci-Fi, Aventura, Accion",
                "El duque Paul Atreides se une a los Fremen y comienza su entrenamiento para convertirse en Muad'Dib, mientras trata de evitar el terrible futuro que ha previsto: una guerra santa en su nombre, esparciéndose por todo el universo conocido.", "poster_dune_part_two", 8.5);

        Pelicula pGodfather = new Pelicula("El Padrino", "Francis Ford Coppola", 1972, 175.0, "Gangster, Crimen, Drama",
                "El envejecido patriarca de una dinastía del crimen organizado en la ciudad de Nueva York de la posguerra transfiere el control de su imperio clandestino a su reacio hijo menor.", "poster_the_godfather", 9.2);

        Pelicula pGodfatherPart2 = new Pelicula("El Padrino Parte 2", "Francis Ford Coppola", 1974, 202.0, "Gangster, Crimen, Drama",
                "Se retratan los inicios de la vida y la carrera de Vito Corleone en el Nueva York de los años 20, mientras su hijo, Michael, amplía y refuerza su control sobre el sindicato del crimen familiar.", "poster_the_godfather_part_two", 9.0);

        Pelicula pWarPlanetApes = new Pelicula("La Guerra Del Planeta de Los Simios", "Matt Reeves", 2017, 140.0, "Sci-Fi, Aventura, Accion",
                "Después de que los simios sufran pérdidas inimaginables, César lucha con sus instintos más oscuros y comienza su propia búsqueda mítica para vengar a su especie.", "poster_war_for_the_planet_of_the_apes", 7.4);

        Pelicula pDawnPlanetApes = new Pelicula("EL Amanecer Del Planeta de Los Simios", "Matt Reeves", 2014, 130.0, "Sci-Fi, Aventura, Accion",
                "Una creciente nación de simios genéticamente evolucionados, liderada por César, está amenazada por una banda de supervivientes humanos del devastador virus que se desató una década atrás.", "poster_dawn_of_the_planet_of_the_apes", 7.6);

        Pelicula pRisePlanetApes = new Pelicula("EL Origen Del Planeta de Los Simios", "Rupert Wyatt", 2011, 105.0, "Sci-Fi, Drama, Accion",
                "Una sustancia diseñada para ayudar al cerebro a repararse a sí mismo da una inteligencia avanzada a un chimpancé que lidera un levantamiento de simios.", "poster_rise_of_the_planet_of_the_apes", 7.6);



        Pelicula pKingdomPlanetApes = new Pelicula("Reino Del Planeta de Los Simios", "Wes Ball", 2024, 145.0, "Sci-Fi, Aventura, Drama",
                "Muchos años después del reinado de César, un joven simio emprende un viaje que lo llevará a cuestionar todo lo que le han enseñado sobre el pasado y a tomar decisiones que definirán el futuro tanto para los simios como para los humanos.", "poster_kingdom_of_the_planet_of_the_apes", 6.9);

        Pelicula pUS = new Pelicula("US", "Jordan Peele", 2019, 116.0, "Terror, Misterio, Thriller",
                "Una familia va a pasar unos dias a su casa de la playa, pero la tranquilidad se vuelve tensión cuando reciben a unos visitantes que no son bienvenidos.", "poster_us", 6.8);

        Pelicula pTwisters = new Pelicula("Twisters", "Lee Isaac Chung", 2024, 122.0, "Desastre, Aventura, Accion",
                "Una actualización de la película de 1996 «Twister», que se centró en un par de cazadores de tormentas que arriesgan sus vidas en un intento de probar un sistema experimental de alerta meteorológica.", "poster_twisters", 6.5);

        Pelicula pTopGunMaverick = new Pelicula("Top Gun Maverick", "Joseph Kosinski", 2022, 130.0, "Accion, Drama, Guerra",
                "Después de más de treinta años de servicio como uno de los mejores aviadores de la Armada, Pete Mitchell está donde pertenece, forzando los límites como valiente piloto de pruebas y esquivando el avance de rango que lo dejaría en tierra.", "poster_top_gun_maverick", 8.2);

        Pelicula pTopGun = new Pelicula("Top Gun", "Tony Scott", 1986, 109.0, "Accion, Drama, Guerra",
                "Mientras los alumnos de la escuela de élite de armas de combate de la Marina de los Estados Unidos compiten por ser los mejores, un joven y atrevido piloto aprende de un instructor civil algunas cosas que no se enseñan en las aulas.", "poster_top_gun", 7.0);

        Pelicula pTerminator = new Pelicula("Terminator", "James Cameron", 1984, 107.0, "Sci-Fi, Accion, Cyberpunk",
                "Un androide aparentemente indestructible viaja desde el año 2029 hasta el año 1984 para asesinar a una camarera cuyo hijo no nacido liderará a la humanidad en una guerra contra las máquinas. Se envía a un combatiente de esa guerra para que proteja a la mujer cueste lo que cueste.", "poster_the_terminator", 8.1);

        Pelicula pStarWars = new Pelicula("Star Wars", "George Lucas", 1977, 121.0, "Sci-Fi, Accion, Drama",
                "Luke Skywalker une sus fuerzas con un caballero jedi, un piloto fanfarrón, un wookiee y dos droides para salvar a la galaxia de la estación espacial del Imperio, a la vez que intenta rescatar a la princesa Leia del malvado Darth Vader.", "poster_star_wars", 8.6);

        Pelicula pEmpireStrikesBack = new Pelicula("El Imperio Contraataca", "Irvin Kershner", 1980, 124.0, "Sci-Fi, Accion, Drama",
                "Tras ser brutalmente dominados los rebeldes por el Imperio en el planeta helado Hoth, Luke Skywalker comienza su entrenamiento jedi con Yoda, mientras sus amigos son perseguidos por Darth Vader y el cazarrecompensas Boba Fett.", "poster_the_empire_strikes_back", 8.7);


        // THE LAST STAR WARS

        Pelicula pTheBatman = new Pelicula("The Batman", "Matt Reeves", 2022, 176.0, "Crimen, Misterio, Thriller",
                "Cuando Enigma, un sádico asesino en serie, comienza a asesinar a las principales figuras políticas de Gotham, Batman se ve obligado a investigar la corrupción oculta de la ciudad y a cuestionar la participación de su familia.", "poster_the_batman", 7.8);



        peliculas.add(pJaws);
        peliculas.add(pPulpFiction);
        peliculas.add(pScarface);
        peliculas.add(pMadMax);
        peliculas.add(pInterstellar);
        peliculas.add(pAlien);
        peliculas.add(pGladiator);
        peliculas.add(pRTOK);
        peliculas.add(pHeat);

        peliculas.add(pDune2021);
        peliculas.add(pDune2);
        peliculas.add(pGodfather);
        peliculas.add(pGodfatherPart2);
        peliculas.add(pWarPlanetApes);
        peliculas.add(pDawnPlanetApes);
        peliculas.add(pRisePlanetApes);

        peliculas.add(pKingdomPlanetApes);
        peliculas.add(pUS);
        peliculas.add(pTwisters);
        peliculas.add(pTopGunMaverick);
        peliculas.add(pTopGun);
        peliculas.add(pTerminator);
        peliculas.add(pStarWars);
        peliculas.add(pEmpireStrikesBack);
        peliculas.add(pTheBatman);







    }

    public static Pelicula getPeliculaPorNombre(String nombre) {
        for (Pelicula p: peliculas) {
            if (p.getNombre().equals(nombre)) {
                return p;
            }
        }
        return null;
    }

    public static void IniciarPeliculasBBDD() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        HashMap<String, Object> peliculaBBDD = new HashMap<>();
        for (Pelicula pe : peliculas) {
            peliculaBBDD.put("nombre", pe.getNombre());
            peliculaBBDD.put("director", pe.getDirector());
            peliculaBBDD.put("año", pe.getAño());
            peliculaBBDD.put("duracion", pe.getDuracion());
            peliculaBBDD.put("descripcion", pe.getDescripcion());
            peliculaBBDD.put("generos", pe.getGeneros());
            peliculaBBDD.put("notaGlobal", pe.getNotaGlobal());
            peliculaBBDD.put("poster", pe.getPoster());

            db.collection("Peliculas")
                    .add(peliculaBBDD)
                    .addOnSuccessListener(documentReference -> {
                        Log.d("Firestore", "DocumentSnapshot added with ID: " + documentReference.getId());
                    })
                    .addOnFailureListener(e -> {
                        Log.w("Firestore", "Error adding document", e);
                    });
        }

    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }

    public void setGeneros(String generos) {
        this.generos = generos;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setNotaGlobal(double notaGlobal) {
        this.notaGlobal = notaGlobal;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDirector() {
        return director;
    }

    public int getAño() {
        return año;
    }

    public double getDuracion() {
        return duracion;
    }

    public String getGeneros() {
        return generos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPoster() {
        return poster;
    }

    public double getNotaGlobal() {
        return notaGlobal;
    }

    public static ArrayList<Pelicula> getPeliculas() {
        return peliculas;
    }


}

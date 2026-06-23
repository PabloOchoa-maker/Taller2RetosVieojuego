
package problema01.problema01;

import java.util.Scanner;

public class Problema01_Ejecutor {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        System.out.println("====================================================");
        System.out.println("  SISTEMA DE COMBATE RPG - SIMULACIÓN DE BATALLA ");
        System.out.println("====================================================\n");

        System.out.println("--- CONFIGURACIÓN DE PERSONAJES ---");
        System.out.print("Ingrese el nombre del Guerrero: ");
        String nomG = teclado.nextLine();
        Problema01_Guerrero guerrero = new Problema01_Guerrero(nomG, 100, 18, 10, 8);

        System.out.print("Ingrese el nombre del Mago: ");
        String nomM = teclado.nextLine();
        Problema01_Mago mago = new Problema01_Mago(nomM, 90, 9, 6, 17);

        System.out.print("Ingrese el nombre del Arquero: ");
        String nomA = teclado.nextLine();
        Problema01_Arquero arquero = new Problema01_Arquero(nomA, 100, 14, 8, 15);

        System.out.println("\n--- CONFIGURACION DE EQUIPAMIENTO ---");
        Arma espada = new Arma("Espada Larga", "Arma", 12);
        Armadura coraza = new Armadura("Coraza de Hierro", "Armadura", 7);
        Arma arco = new Arma("Arco Elfico", "Arma", 9);

        guerrero.agregarObjeto(espada);
        guerrero.equipar(espada);

        mago.agregarObjeto(coraza);
        mago.equipar(coraza);

        arquero.agregarObjeto(arco);
        arquero.equipar(arco);

        System.out.println(guerrero.getNombre() + " equipa " + espada.getNombre() + " (+"
                + espada.getModificadorAtaque() + " ataque)");
        System.out.println(mago.getNombre() + " equipa " + coraza.getNombre() + " (+"
                + coraza.getModificadorDefensa() + " defensa)");
        System.out.println(arquero.getNombre() + " equipa " + arco.getNombre() + " (+"
                + arco.getModificadorAtaque() + " ataque)");

        System.out.println("\n--- ESTADO INICIAL DE LOS HEROES ---");
        System.out.println(guerrero.toString());
        System.out.println(mago.toString());
        System.out.println(arquero.toString());
        

        System.out.println("--------------BATALLA 1:ESCOGA LOS PELEADORES--------------");
        while (true) {
            System.out.println("Seleccione el primer peleador (1: Guerrero, 2: Mago, 3: Arquero): ");
            int opcion1 = teclado.nextInt();
            System.out.println("Seleccione el segundo peleador (1: Guerrero, 2: Mago, 3: Arquero): ");
            int opcion2 = teclado.nextInt();
            teclado.nextLine(); // Limpiar buffer

            if (opcion1 == opcion2) {
                System.out.println("No puede seleccionar el mismo personaje para ambos lados. Intente nuevamente.");
                continue;
            }

            Problema01_Personaje p1 = null;
            Problema01_Personaje p2 = null;

            switch (opcion1) {
                case 1 -> p1 = guerrero;
                case 2 -> p1 = mago;
                case 3 -> p1 = arquero;
                default -> {
                    System.out.println("Opción inválida para el primer peleador. Intente nuevamente.");
                    continue;
                }
            }

            switch (opcion2) {
                case 1 -> p2 = guerrero;
                case 2 -> p2 = mago;
                case 3 -> p2 = arquero;
                default -> {
                    System.out.println("Opción inválida para el segundo peleador. Intente nuevamente.");
                    continue;
                }
            }

            ejecutarCombate(p1, p2, teclado);
            break; // Salir del bucle después de una batalla válida
        }

        System.out.println("\n--- ESTADOS POST-COMBATE Y REVISIÓN DE NIVELES ---");
        System.out.println(guerrero.toString());
        System.out.println(mago.toString());
        System.out.println(arquero.toString());


        System.out.println("--------------BATALLA 2: EL SUPERVIVIENTE VS ARQUERO--------------");


        Problema01_Personaje superviviente = guerrero.estaVivo() ? guerrero : mago;
        if (!superviviente.estaVivo()) {
            System.out.println("Ambos contendientes iniciales cayeron en batalla.");
        } else {
            ejecutarCombate(superviviente, arquero, teclado);
        }

        System.out.println("-----------------FIN DE LA SIMULACIÓN-----------------");
        teclado.close();
    }

    private static void ejecutarCombate(Problema01_Personaje p1, Problema01_Personaje p2, Scanner teclado) {
        System.out.println("\n¡COMIENZA EL DUELO ENTRE " + p1.getNombre().toUpperCase() + " Y "
                + p2.getNombre().toUpperCase() + "!");
        System.out.println("Habilidad Especial de " + p1.getNombre() + ": " + p1.obtenerHabilidadEspecial());
        System.out.println("Habilidad Especial de " + p2.getNombre() + ": " + p2.obtenerHabilidadEspecial());
        System.out.println("Energía de " + p1.getNombre() + ": " + p1.getEnergia() + "/" + p1.getEnergiaMaxima()
                + " | Costo habilidad: " + p1.getCostoEnergiaHabilidad() + " | Cooldown: " + p1.getCooldownHabilidad() + " turnos");
        System.out.println("Energía de " + p2.getNombre() + ": " + p2.getEnergia() + "/" + p2.getEnergiaMaxima()
                + " | Costo habilidad: " + p2.getCostoEnergiaHabilidad() + " | Cooldown: " + p2.getCooldownHabilidad() + " turnos");
        System.out.println("\nPresione ENTER para procesar los turnos de combate...");
        teclado.nextLine();

        int turno = 1;
        while (p1.estaVivo() && p2.estaVivo()) {
            System.out.println("--- Turno " + turno + " ---");

            // Turno de P1: primero se procesan sus estados (veneno, aturdimiento, buff)
            realizarTurno(p1, p2);
            if (!p2.estaVivo()) {
                System.out.println(" ¡" + p2.getNombre() + " ha sido derrotado!");
                break;
            }
            if (!p1.estaVivo()) {
                break; // P1 pudo caer por el daño de sus propios estados
            }

            // Turno de P2
            realizarTurno(p2, p1);
            if (!p1.estaVivo()) {
                System.out.println(" ¡" + p1.getNombre() + " ha sido derrotado!");
                break;
            }
            if (!p2.estaVivo()) {
                break;
            }

            System.out.println("Estado actual: " + p1.getNombre() + " (" + p1.getPuntosVida() + " PV, "
                    + p1.getEnergia() + " EN) | "
                    + p2.getNombre() + " (" + p2.getPuntosVida() + " PV, " + p2.getEnergia() + " EN)");
            turno++;
            System.out.println();
        }

        if (p1.estaVivo()) {
            System.out.println("\n¡GANADOR: " + p1.getNombre() + "!");
            System.out.println(p1.getNombre() + " recibe 100 puntos de experiencia y sube de nivel.");
            p1.ganarExperiencia(100);
        } else if (p2.estaVivo()) {
            System.out.println("\n¡GANADOR: " + p2.getNombre() + "!");
            System.out.println(p2.getNombre() + " recibe 100 puntos de experiencia y sube de nivel.");
            p2.ganarExperiencia(100);
        } else {
            System.out.println("\n¡EMPATE! Ambos personajes cayeron en combate.");
        }
    }

    private static void realizarTurno(Problema01_Personaje atacante, Problema01_Personaje objetivo) {
        boolean puedeAtacar = atacante.procesarEstadosYVerificarTurno();

        // El daño por turno (veneno/quemadura) pudo derrotar al atacante en su propio turno
        if (!atacante.estaVivo()) {
            System.out.println(" ¡" + atacante.getNombre() + " ha caído por efecto de sus estados!");
            return;
        }
        if (!puedeAtacar) {
            return; // Incapacitado: pierde el turno (el mensaje ya fue impreso)
        }

        // Al inicio del turno se regenera energía y se reduce el cooldown
        atacante.regenerarRecursos();

        // Se intenta usar la habilidad especial: requiere cooldown == 0 y energía suficiente
        if (atacante.habilidadDisponible()) {
            try {
                int danoEspecial = atacante.usarHabilidadEspecial(objetivo);
                System.out.println(" -> " + atacante.getNombre() + " ejecuta su habilidad especial "
                        + atacante.obtenerHabilidadEspecial() + " causando " + danoEspecial + " de daño."
                        + " (Energía: " + atacante.getEnergia() + "/" + atacante.getEnergiaMaxima()
                        + " | Recarga: " + atacante.getCooldownActual() + " turno(s))");
                // La habilidad especial aplica su estado al concretarse
                aplicarEstadoHabilidad(atacante, objetivo);
                return;
            } catch (Problema01_EnergiaInsuficienteException e) {
                System.out.println(" -> [SIN ENERGÍA] " + e.getMessage());
                System.out.println("    " + atacante.getNombre() + " recurre a un ataque básico.");
            }
        } else {
            System.out.println(" -> [EN RECARGA] " + atacante.obtenerHabilidadEspecial() + " de "
                    + atacante.getNombre() + " no está disponible (" + atacante.getCooldownActual()
                    + " turno(s) restantes). Realiza un ataque básico.");
        }

        // Ataque básico de respaldo: no consume energía ni aplica estados
        int ataque = atacante.calcularAtaque();
        int vidaAntes = objetivo.getPuntosVida();
        objetivo.recibirDano(ataque);
        int danoReal = vidaAntes - objetivo.getPuntosVida();
        System.out.println(" -> " + atacante.getNombre() + " realiza un ataque básico causando "
                + danoReal + " de daño efectivo.");
    }
 
    private static void aplicarEstadoHabilidad(Problema01_Personaje atacante, Problema01_Personaje objetivo) {
        switch (atacante.obtenerHabilidadEspecial()) {
            case "Bola de Fuego" ->
                objetivo.aplicarEstado(new Problema01_Estados("Quemadura", 3, "DAN_TURNO", 5));
            case "Lluvia de Flechas" ->
                objetivo.aplicarEstado(new Problema01_Estados("Aturdimiento", 2, "INCAPACITAR", 0));
            case "Carga con Escudo" ->
                atacante.aplicarEstado(new Problema01_Estados("Furia de Batalla", 3, "BUFF_MULTIPLIQUEN", 0));
            default -> {
                // Sin estado asociado
            }
        }
    }
}

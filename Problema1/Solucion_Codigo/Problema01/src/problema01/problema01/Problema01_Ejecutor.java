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
        Problema01_Personaje guerrero = new Problema01_Guerrero(nomG, 100, 18, 10, 8);

        System.out.print("Ingrese el nombre del Mago: ");
        String nomM = teclado.nextLine();
        Problema01_Personaje mago = new Problema01_Mago(nomM, 90, 9, 6, 17);

        System.out.print("Ingrese el nombre del Arquero: ");
        String nomA = teclado.nextLine();
        Problema01_Personaje arquero = new Problema01_Arquero(nomA, 100, 14, 8, 15);

        System.out.println("\nSeleccione quiénes van a combatir:");
        System.out.println("1. Guerrero vs Mago");
        System.out.println("2. Guerrero vs Arquero");
        System.out.println("3. Mago vs Arquero");
        System.out.print("Opción: ");
        int opcion = teclado.nextInt();

        Problema01_Personaje p1 = guerrero;
        Problema01_Personaje p2 = mago;

        if (opcion == 2) {
            p2 = arquero;
        } else if (opcion == 3) {
            p1 = mago;
            p2 = arquero;
        }

        System.out.println("\n--- ESTADO INICIAL DE LOS HEROES ---");
        System.out.println("P1: " + p1.getNombre() + " - PV: " + p1.getPuntosVida() + ", Fuerza: " + p1.getFuerza());
        System.out.println("P2: " + p2.getNombre() + " - PV: " + p2.getPuntosVida() + ", Fuerza: " + p2.getFuerza());
        System.out.println("====================================================\n");

        System.out.println("--- APLICACIÓN DE EFECTOS INICIALES EN LA ARENA ---");
        p1.aplicarEstado(new Problema01_Estados("Veneno Letal", 3, "DAN_TURNO", 10));
        p1.aplicarEstado(new Problema01_Estados("Furia de Batalla", 2, "BUFF_MULTIPLIQUEN", 0));
        p2.aplicarEstado(new Problema01_Estados("Parálisis Eléctrica", 1, "INCAPACITAR", 0));
        System.out.println("====================================================\n");

        System.out.println("¡EMPIEZA EL COMBATE EN LA ARENA!\n");
        int turno = 1;

        while (p1.estaVivo() && p2.estaVivo()) {
            System.out.println("--- Turno " + turno + " ---");

            if (p1.procesarEstadosYVerificarTurno()) {
                int ataqueP1 = p1.calcularAtaque(); 
                int vidaAntesP2 = p2.getPuntosVida();
                p2.recibirDano(ataqueP1);
                int danoRealP2 = vidaAntesP2 - p2.getPuntosVida();
                System.out.println(" -> " + p1.getNombre() + " ejecuta " + p1.obtenerHabilidadEspecial() 
                        + " causando " + danoRealP2 + " de daño efectivo.");
            }

            if (!p2.estaVivo()) {
                System.out.println(" ¡" + p2.getNombre() + " ha sido derrotado!");
                break;
            }

            if (p2.procesarEstadosYVerificarTurno()) {
                int ataqueP2 = p2.calcularAtaque(); 
                int vidaAntesP1 = p1.getPuntosVida();
                p1.recibirDano(ataqueP2);
                int danoRealP1 = vidaAntesP1 - p1.getPuntosVida();
                System.out.println(" -> " + p2.getNombre() + " responde con " + p2.obtenerHabilidadEspecial() 
                        + " causando " + danoRealP1 + " de daño efectivo.");
            }

            if (!p1.estaVivo()) {
                System.out.println(" ¡" + p1.getNombre() + " ha sido derrotado!");
                break;
            }

            System.out.println("Estado actual: " + p1.getNombre() + " (" + p1.getPuntosVida() + " PV) | "
                    + p2.getNombre() + " (" + p2.getPuntosVida() + " PV)");
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
        }
    }
}
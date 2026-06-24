package problema01.problema01;

import java.util.ArrayList;

public abstract class Problema01_Personaje {
    protected String nombre;
    protected int nivel;
    protected int puntosVida;
    protected int puntosVidaMaximos;
    protected int fuerza;
    protected int defensa;
    protected int experiencia;
    protected ArrayList<Objeto> inventario;
    protected Objeto objetoEquipado;

    // --- Sistema de Maná/Energía y Cooldown ---
    protected int energia;
    protected int energiaMaxima;
    protected int cooldownActual;

    /** Energía que el personaje recupera al inicio de cada uno de sus turnos. */
    protected static final int REGENERACION_ENERGIA = 15;

    protected java.util.List<Problema01_Estados> listaEstados = new java.util.ArrayList<>();

    // ============================================================
    //   CONSTRUCTOR
    // ============================================================

    public Problema01_Personaje(String nombre, int puntosVidaMaximos, int fuerza, int defensa) {
        this.nombre = nombre;
        this.nivel = 1;
        this.puntosVidaMaximos = puntosVidaMaximos;
        this.puntosVida = puntosVidaMaximos;
        this.fuerza = fuerza;
        this.defensa = defensa;
        this.experiencia = 0;
        this.inventario = new ArrayList<Objeto>();
        this.objetoEquipado = null;
        this.energiaMaxima = 100;
        this.energia = this.energiaMaxima;
        this.cooldownActual = 0;
    }

    // ============================================================
    //   MÉTODOS ABSTRACTOS
    // ============================================================

    public abstract int obtenerDanoBase();

    public abstract int calcularDefensa();
    public abstract String obtenerHabilidadEspecial();

    /** Energía (maná) que cuesta lanzar la habilidad especial. */
    public abstract int getCostoEnergiaHabilidad();

    /** Turnos de recarga (cooldown) tras usar la habilidad especial. */
    public abstract int getCooldownHabilidad();

    // ============================================================
    //   INVENTARIO Y EQUIPAMIENTO
    // ============================================================

    public void agregarObjeto(Objeto objeto) {
        this.inventario.add(objeto);
    }

    public void equipar(Objeto objeto) {
        if (this.inventario.contains(objeto)) {
            this.objetoEquipado = objeto;
        }
    }

    protected int getBonusAtaque() {
        if (objetoEquipado != null) {
            return objetoEquipado.getModificadorAtaque();
        }
        return 0;
    }

    protected int getBonusDefensa() {
        if (objetoEquipado != null) {
            return objetoEquipado.getModificadorDefensa();
        }
        return 0;
    }

    // ============================================================
    //   SISTEMA DE ESTADOS (efectos temporales)
    // ============================================================

    public void aplicarEstado(Problema01_Estados nuevoEstado) {
        this.listaEstados.add(nuevoEstado);
        System.out.println(" -> [ESTADO] ¡" + this.nombre + " se ve afectado por " + nuevoEstado.getNombre() + " por " + nuevoEstado.getDuracion() + " turnos!");
    }

    public boolean tieneEstadoActivo(String tipo) {
        for (Problema01_Estados estado : listaEstados) {
            if (estado.getTipo().equals(tipo) && estado.getDuracion() > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean procesarEstadosYVerificarTurno() {
        boolean puedeAtacar = true;
        java.util.Iterator<Problema01_Estados> iterador = listaEstados.iterator();

        while (iterador.hasNext()) {
            Problema01_Estados estado = iterador.next();

            if (estado.getTipo().equals("DAN_TURNO")) {
                int danoVeneno = estado.getEfecto();
                this.puntosVida -= danoVeneno;
                if (this.puntosVida < 0) {
                    this.puntosVida = 0;
                }
                System.out.println(" -> [" + estado.getNombre().toUpperCase() + "] " + this.nombre
                        + " sufre " + danoVeneno + " de daño por veneno. (PV restantes: " + this.puntosVida + ")");
            }

            if (estado.getTipo().equals("INCAPACITAR")) {
                System.out.println(" -> [" + estado.getNombre().toUpperCase() + "] " + this.nombre + " está incapacitado y pierde su turno.");
                puedeAtacar = false;
            }

            if (estado.getTipo().equals("BUFF_MULTIPLIQUEN")) {
                System.out.println(" -> [" + estado.getNombre().toUpperCase() + "] " + this.nombre + " mantiene su ataque potenciado (*1.25).");
            }

            estado.reducirDuracion();


            if (estado.getDuracion() <= 0) {
                System.out.println(" -> [INFO] El estado " + estado.getNombre() + " ha expirado en " + this.nombre);
                iterador.remove();
            }
        }


        return this.estaVivo() && puedeAtacar;
    }

    // ============================================================
    //   COMBATE
    // ============================================================

    public int calcularAtaque() {
        int danoFinal = obtenerDanoBase() + getBonusAtaque();

        if (tieneEstadoActivo("BUFF_MULTIPLIQUEN")) {
            danoFinal = (int) (danoFinal * 1.25);
        }

        return danoFinal;
    }

    /**
     * Ejecuta la habilidad especial contra un objetivo. Valida el cooldown y,
     * sobre todo, la energía: si no hay maná suficiente lanza una excepción.
     *
     * @return el daño efectivo causado al objetivo.
     * @throws Problema01_EnergiaInsuficienteException si falta energía.
     * @throws IllegalStateException si la habilidad está en recarga.
     */
    public int usarHabilidadEspecial(Problema01_Personaje objetivo)
            throws Problema01_EnergiaInsuficienteException {
        if (!habilidadDisponible()) {
            throw new IllegalStateException("La habilidad '" + obtenerHabilidadEspecial()
                    + "' está en recarga (" + this.cooldownActual + " turno(s) restantes).");
        }
        if (!tieneEnergiaSuficiente()) {
            throw new Problema01_EnergiaInsuficienteException(this.nombre,
                    obtenerHabilidadEspecial(), getCostoEnergiaHabilidad(), this.energia);
        }

        this.energia -= getCostoEnergiaHabilidad();
        this.cooldownActual = getCooldownHabilidad();

        // La habilidad especial golpea con más fuerza que un ataque básico.
        int danoEspecial = (int) (calcularAtaque() * 1.5);
        int vidaAntes = objetivo.getPuntosVida();
        objetivo.recibirDano(danoEspecial);
        return vidaAntes - objetivo.getPuntosVida();
    }

    public void recibirDano(int danoRecibido) {
        int danoEfectivo = danoRecibido - calcularDefensa();
        if (danoEfectivo < 1) {
            danoEfectivo = 1;
        }
        this.puntosVida -= danoEfectivo;
        if (this.puntosVida < 0) {
            this.puntosVida = 0;
        }
    }

    public boolean estaVivo() {
        return this.puntosVida > 0;
    }

    // ============================================================
    //   SISTEMA DE MANÁ/ENERGÍA Y COOLDOWN
    // ============================================================

    /**
     * Recupera energía y reduce el cooldown. Debe invocarse al inicio de
     * cada turno del personaje para gestionar sus estados temporales.
     */
    public void regenerarRecursos() {
        if (this.energia < this.energiaMaxima) {
            this.energia += REGENERACION_ENERGIA;
            if (this.energia > this.energiaMaxima) {
                this.energia = this.energiaMaxima;
            }
        }
        if (this.cooldownActual > 0) {
            this.cooldownActual--;
        }
    }

    /** La habilidad está disponible solo cuando no está en recarga. */
    public boolean habilidadDisponible() {
        return this.cooldownActual == 0;
    }

    /** Indica si el personaje dispone de energía para lanzar la habilidad. */
    public boolean tieneEnergiaSuficiente() {
        return this.energia >= getCostoEnergiaHabilidad();
    }

    // ============================================================
    //   PROGRESIÓN
    // ============================================================

    public void ganarExperiencia(int exp) {
        this.experiencia += exp;
        if (this.experiencia >= 100) {
            this.nivel++;
            this.experiencia -= 100;
            this.puntosVidaMaximos += 20;
            this.fuerza += 5;
            this.defensa += 3;
            this.puntosVida = this.puntosVidaMaximos;
        }
    }

    // ============================================================
    //   GETTERS
    // ============================================================

    public String getNombre() { return nombre; }
    public int getNivel() { return nivel; }
    public int getPuntosVida() { return puntosVida; }
    public int getPuntosVidaMaximos() { return puntosVidaMaximos; }
    public int getFuerza() { return fuerza; }
    public int getDefensa() { return defensa; }
    public int getExperiencia() { return experiencia; }
    public int getEnergia() { return energia; }
    public int getEnergiaMaxima() { return energiaMaxima; }
    public int getCooldownActual() { return cooldownActual; }
    public Objeto getObjetoEquipado() { return objetoEquipado; }
    public ArrayList<Objeto> getInventario() { return inventario; }

    // ============================================================
    //   REPRESENTACIÓN
    // ============================================================

    @Override
    public String toString() {
        String equipo = "Ninguno";
        if (objetoEquipado != null) {
            equipo = objetoEquipado.getNombre();
        }
        return String.format("[%s] %s - Nivel: %d | PV: %d/%d | Energía: %d/%d | Fuerza: %d | Defensa: %d | EXP: %d/100 | Equipado: %s",
                this.getClass().getSimpleName().replace("Problema01_", ""),
                nombre, nivel, puntosVida, puntosVidaMaximos, energia, energiaMaxima, fuerza, defensa, experiencia, equipo);
    }
}

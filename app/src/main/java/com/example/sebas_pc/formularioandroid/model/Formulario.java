package com.example.sebas_pc.formularioandroid.model;

/**
 * Created by SEBAS-PC on 09/01/2018.
 */

//git remote add origin ssh://git@github.com/ZombieProgrammer/FormularioAndroid
//FORMULARIO
public class Formulario {
    public String postKey;
    public String AAdhForm;
    public String ABid_movil;
    public String ACdNI;
    public String ADnombre;
    public String AEapellidos;
    public String AFinici;
    public String AGfi;
    public String AHhores;
    public String AIdestinatari;
    public String AJarea;
    public String AKambit;
    public String ALfamiliar;
    public String AMtipus;
    public String ANtipusF;
    public String AOobservaciones;
    public boolean AQcheck;
    public String ARimg;


    public Formulario(String dhForm, String id_movil, String DNI, String nombre, String apellidos,
                      String inici, String fi, String hores, String destinatari, String ambit,
                      String familiar, String tipus, String tipusF, Boolean check, String img) {

        this.AAdhForm = dhForm;
        this.ABid_movil = id_movil;
        this.ACdNI = DNI;
        this.ADnombre = nombre;
        this.AEapellidos = apellidos;
        this.AFinici = inici;
        this.AGfi = fi;
        this.AHhores = hores;
        this.AIdestinatari = destinatari;
        this.AKambit = ambit;
        this.ALfamiliar = familiar;
        this.AMtipus = tipus;
        this.ANtipusF = tipusF;
        this.AQcheck = check;
        this.ARimg = img;
    }

    public Formulario(String postKey, String dni, String nombre, String apellidos){
        this.postKey=postKey;
        this.ACdNI = dni;
        this.ADnombre = nombre;
        this.AEapellidos = apellidos;
    }

    public Formulario() {

    }

    @Override
    public String toString(){
        return "  \nFormulario"
                + "\n   --------------"
                + "\n1- " + AAdhForm
                + "\n2- " + ABid_movil
                + "\n3- " + ACdNI
                + "\n4- " + ADnombre
                + "\n5- " + AEapellidos
                + "\n6- " + AFinici
                + "\n7- " + AGfi
                + "\n8- " + AHhores
                + "\n9- " + AIdestinatari
                + "\n10- " + AJarea
                + "\n11- " + AKambit
                + "\n12- " + ALfamiliar
                + "\n13- " + AMtipus
                + "\n14- " + ANtipusF
                + "\n   --------------\n\n";
    }

    public String toString2(){
        return "  \nFormulario"
                + "\n   --------------"
                + "\n1- " + AAdhForm
                + "\n4- " + ADnombre
                + "\n5- " + AEapellidos
                + "\n   --------------\n\n";
    }

}

package com.mycompany.e.trenuri;

public class Ora implements Comparable<Ora> {

    private int minut;
    private int ora;

    public Ora(int ora, int minut) {
        this.ora = ora;
        this.minut = minut;
    }

    public int getOra() {
        return ora;
    }

    public int getMinut() {
        return minut;
    }

    public void adaugaMinute(int minute) {
        this.minut += minute;
        this.ora += this.minut / 60;
        this.minut = this.minut % 60;
        this.ora = this.ora % 24;
    }
    public void Minus(Ora x){
        minut -= x.getMinut();
        if(minut < 0 ){
            minut += 60;
            ora--;
        }
        ora-= x.getOra();
        if(ora<0){
            ora+=24;
        }
        
    }

    public void adaugaOre(int ore) {
        this.ora += ore;
        this.ora = this.ora % 24;
    }

    boolean isGreaterThan(Ora that) {
        return this.compareTo(that) > 0;
    }

    boolean isLessThan(Ora that) {
        return this.compareTo(that) < 0;
    }

    @Override
    public String toString() {
        return (ora <= 9? "0"+ora : ora)+":"+(minut <= 9? "0"+minut : minut); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

   
    
    @Override
    public int compareTo(Ora o) {
        return this.ora == o.getOra() ? this.minut - o.getMinut() : this.ora - o.getOra();
    }
    
    

}

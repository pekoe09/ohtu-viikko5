
package ohtu.intjoukkosovellus;

import java.util.Arrays;

public class IntJoukko {

    public final static int KAPASITEETTI = 5, // aloitustalukon koko
                            OLETUSKASVATUS = 5;  // luotava uusi taulukko on 
    // näin paljon isompi kuin vanha
    private int kasvatuskoko;     // Uusi taulukko on tämän verran vanhaa suurempi.
    private int[] joukko;      // Joukon luvut säilytetään taulukon alkupäässä. 
    private int alkioidenLkm;    // Tyhjässä joukossa alkioiden_määrä on nolla. 

    public IntJoukko() {
        joukko = new int[KAPASITEETTI];
        alustaAlkiot();        
        this.kasvatuskoko = OLETUSKASVATUS;
    }

    public IntJoukko(int kapasiteetti) {
        if (kapasiteetti < 0) {
            throw new IndexOutOfBoundsException("Kapasitteetti väärin");
        }
        joukko = new int[kapasiteetti];
        alustaAlkiot();
        this.kasvatuskoko = OLETUSKASVATUS;

    }    
    
    public IntJoukko(int kapasiteetti, int kasvatuskoko) {
        if (kapasiteetti < 0) {
            throw new IndexOutOfBoundsException("Kapasitteetti väärin");//heitin vaan jotain :D
        }
        if (kasvatuskoko < 0) {
            throw new IndexOutOfBoundsException("kapasiteetti2");//heitin vaan jotain :D
        }
        joukko = new int[kapasiteetti];
        alustaAlkiot();
        this.kasvatuskoko = kasvatuskoko;
    }
    
    private void alustaAlkiot() { //ok
        for (int i = 0; i < joukko.length; i++) {
            joukko[i] = 0;
        }
        alkioidenLkm = 0;
    }

    public void lisaa(int luku) { // ok
        if (sijainti(luku) > -1) 
            return;
        
        joukko[alkioidenLkm] = luku;
        alkioidenLkm++;

        if (alkioidenLkm == joukko.length) 
            kasvataTaulukkoa();
    }
    
    private void kasvataTaulukkoa() { // ok
        int[] taulukkoOld = joukko.clone();
        joukko = new int[alkioidenLkm + kasvatuskoko];
        kopioiTaulukko(taulukkoOld, joukko);
    }

    public int sijainti(int luku) { // ok
        return sijainti(luku, toIntArray());
    }
    
    public static int sijainti(int luku, int[] taulukko) {
        for (int i = 0; i < taulukko.length; i++) {
            if (luku == taulukko[i]) 
                return i;            
        }
        return -1;
    }

    public void poista(int luku) {
        int kohta = sijainti(luku);
        if (kohta > -1) {
            for (int j = kohta; j < alkioidenLkm - 1; j++) {
                joukko[j] = joukko[j + 1];
            }
            alkioidenLkm--;
        }
    }

    private void kopioiTaulukko(int[] vanha, int[] uusi) {
        for (int i = 0; i < vanha.length; i++) {
            uusi[i] = vanha[i];
        }
    }

    public int mahtavuus() {  // ok
        return alkioidenLkm;
    }

    @Override
    public String toString() {  // ok
            String tuotos = "{";
            for (int i = 0; i < alkioidenLkm; i++) {
                tuotos += joukko[i] + ", ";
            }
            if(tuotos.endsWith(", "))
                tuotos = tuotos.substring(0, tuotos.length() - 2);
            tuotos += "}";
            return tuotos;        
    }

    public int[] toIntArray() {  // ok
        return Arrays.copyOfRange(joukko, 0, alkioidenLkm);
    }   

    public static IntJoukko yhdiste(IntJoukko a, IntJoukko b) { // ok
        IntJoukko x = new IntJoukko();
        lisaaKaikkiAlkiot(a.toIntArray(), x);
        lisaaKaikkiAlkiot(b.toIntArray(), x);
        return x;
    }
    
    private static void lisaaKaikkiAlkiot(int[] lahde, IntJoukko kohde)  { // ok
        for (int i = 0; i < lahde.length; i++) {
            kohde.lisaa(lahde[i]);
        }
    }

    public static IntJoukko leikkaus(IntJoukko a, IntJoukko b) {
        IntJoukko y = new IntJoukko();
        int[] aTaulu = a.toIntArray();
        int[] bTaulu = b.toIntArray();
        for (int i = 0; i < aTaulu.length; i++) {  
            if(sijainti(aTaulu[i], bTaulu) > -1)
               y.lisaa(aTaulu[i]);
        }
        return y;
    }
    
    public static IntJoukko erotus ( IntJoukko a, IntJoukko b) {
        IntJoukko z = new IntJoukko();
        int[] aTaulu = a.toIntArray();
        int[] bTaulu = b.toIntArray();
        for (int i = 0; i < aTaulu.length; i++) {  
            if(sijainti(aTaulu[i], bTaulu) == -1)
               z.lisaa(aTaulu[i]);
        }
        return z;
    }
        
}
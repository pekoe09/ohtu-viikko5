import ohtu.verkkokauppa.Kauppa;
import ohtu.verkkokauppa.Pankki;
import ohtu.verkkokauppa.Tuote;
import ohtu.verkkokauppa.Varasto;
import ohtu.verkkokauppa.Viitegeneraattori;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;


public class KauppaTest {
    
    Pankki pankki;
    Viitegeneraattori viite;
    Varasto varasto;
    
    @Before
    public void setUp() {
        pankki = mock(Pankki.class);
        viite = mock(Viitegeneraattori.class);
        varasto = mock(Varasto.class);
    }

    @Test
    public void yhdenTuotteenOstoOnnistuu() {
        when(viite.uusi()).thenReturn(30);
        when(varasto.saldo(1)).thenReturn(12);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "juhlamokka", 5));
         
        Kauppa k = new Kauppa(varasto, pankki, viite);
         
        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.tilimaksu("pekka", "12345");
         
        verify(pankki).tilisiirto(eq("pekka"), eq(30), eq("12345"), anyString(), eq(5));
    }
     
    @Test
    public void kahdenTuotteenOstoOnnistuu() {
        when(viite.uusi()).thenReturn(30);
        when(varasto.saldo(1)).thenReturn(12);
        when(varasto.saldo(2)).thenReturn(1);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "juhlamokka", 5));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "embo", 8));
         
        Kauppa k = new Kauppa(varasto, pankki, viite);
         
        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.lisaaKoriin(2);
        k.tilimaksu("pekka", "12345");
         
        verify(pankki).tilisiirto(eq("pekka"), eq(30), eq("12345"), anyString(), eq(13));
    }
    
    @Test
    public void loppunuttaTuotettaEiOsteta() {
        when(viite.uusi()).thenReturn(30);
        when(varasto.saldo(1)).thenReturn(12);
        when(varasto.saldo(2)).thenReturn(0);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "juhlamokka", 5));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "embo", 8));
         
        Kauppa k = new Kauppa(varasto, pankki, viite);
         
        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.lisaaKoriin(2);
        k.tilimaksu("pekka", "12345");
         
        verify(pankki).tilisiirto(eq("pekka"), eq(30), eq("12345"), anyString(), eq(5));
    }
    
    @Test
    public void asioinninAloitusNollaaEdellisenOstoksenTiedot() {
        when(viite.uusi()).thenReturn(30).thenReturn(31);
        when(varasto.saldo(1)).thenReturn(12);
        when(varasto.saldo(2)).thenReturn(1);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "juhlamokka", 5));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "embo", 8));

        Kauppa k = new Kauppa(varasto, pankki, viite);
        
        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.lisaaKoriin(1);
        k.tilimaksu("pekka", "12345");
        
        verify(pankki).tilisiirto(eq("pekka"), anyInt(), eq("12345"), anyString(), eq(10));
        
        k.aloitaAsiointi();
        k.lisaaKoriin(2);
        k.tilimaksu("veikko", "67890");
        
        verify(pankki).tilisiirto(eq("veikko"), anyInt(), eq("67890"), anyString(), eq(8));
                
    }    
        
    @Test
    public void uusiMaksutapahtumaSaaUudenViitenumeron() {
        when(viite.uusi()).thenReturn(30).thenReturn(31);
        when(varasto.saldo(1)).thenReturn(12);
        when(varasto.saldo(2)).thenReturn(1);
        when(varasto.haeTuote(1)).thenReturn(new Tuote(1, "juhlamokka", 5));
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "embo", 8));

        Kauppa k = new Kauppa(varasto, pankki, viite);
        
        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.lisaaKoriin(1);
        k.tilimaksu("pekka", "12345");
        
        verify(pankki).tilisiirto(anyString(), eq(30), anyString(), anyString(), anyInt());
        
        k.aloitaAsiointi();
        k.lisaaKoriin(2);
        k.tilimaksu("veikko", "67890");
        
        verify(pankki).tilisiirto(anyString(), eq(31), anyString(), anyString(), anyInt());
                
    }
    
    @Test
    public void tuotteenPoistoKoristaOnnistuu() {
        Tuote kahvi = new Tuote(1, "juhlamokka", 5);
        when(viite.uusi()).thenReturn(30);
        when(varasto.saldo(1)).thenReturn(12);
        when(varasto.saldo(2)).thenReturn(1);
        when(varasto.haeTuote(1)).thenReturn(kahvi);
        when(varasto.haeTuote(2)).thenReturn(new Tuote(2, "embo", 8));

        Kauppa k = new Kauppa(varasto, pankki, viite);
        
        k.aloitaAsiointi();
        k.lisaaKoriin(1);
        k.lisaaKoriin(2);
        k.poistaKorista(1);
        k.tilimaksu("pekka", "12345");

        verify(varasto, times(1)).palautaVarastoon(kahvi);
        verify(pankki).tilisiirto(anyString(), anyInt(), anyString(), anyString(), eq(8));
    }
}

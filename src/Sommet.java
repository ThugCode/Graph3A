
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Sommet {

    private String nom;
	private boolean marque;
	private boolean couleur;
	private int valeur;
    private List<Sommet> sommetAdjacent;
    private int degres;
    

    public Sommet(String p_nom) {
        setNom(p_nom);
        setMarque(false);
        setCouleur(false);
        setValeur(0);
        setSommetAdjacent(new ArrayList<Sommet>());
    }

    public void ajouterVoisin(Sommet v) {
    	sommetAdjacent.add(v);
    	setDegres(getDegres()+1);
    }
    
    public String toString() {
        return "Sommet "+nom;
    }

    public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public boolean isMarque() {
		return marque;
	}

	public void setMarque(boolean marque) {
		this.marque = marque;
	}

	public int getValeur() {
		return valeur;
	}

	public void setValeur(int valeur) {
		this.valeur = valeur;
	}

	public List<Sommet> getSommetAdjacent() {
		return sommetAdjacent;
	}

	public void setSommetAdjacent(List<Sommet> sommetAdjacent) {
		this.sommetAdjacent = sommetAdjacent;
		this.degres = sommetAdjacent.size();
	}

	public int getDegres() {
		return degres;
	}

	public void setDegres(int degres) {
		this.degres = degres;
	}
	
	public boolean isCouleur() {
		return couleur;
	}

	public void setCouleur(boolean couleur) {
		this.couleur = couleur;
	}
	
	
	
	
	
	
	
	//TODO
	public static void main(String[] args) {
			
		Graph graph = new Graph();
		graph.lectureFichier("motsdelongueur6.txt");
		System.out.println("1) Graph construit");
		System.out.println("");
		System.out.println("2) Le nombre de sommet est de "+graph.getSommets().size());
		System.out.println("   Le nombre d'arc est de "+graph.getArcs().size());
		System.out.println("");
		List<List<Sommet>> connexe = graph.nombreComposantesConnexes();
		System.out.println("3) Le nombre de composantes connexes est de " + connexe.size());
		System.out.println("");
		System.out.println("4) Le nombre de mots sans voisin est de " + graph.nbComposante_N_mots(connexe, 1));
		System.out.println("");
		System.out.println("5) Le nombre de composantes composées uniquement de deux mots est de " + graph.nbComposante_N_mots(connexe, 2));
		System.out.println("");
		HashMap<Integer, Integer> listeDegre = graph.nbSommet_K_Voisins();
		System.out.println("6) La liste des sommets avec k voisins (k variant de 1 à 28) :");
		System.out.println("   Degré=NbSommet =>" + listeDegre);
		System.out.println("");
		List<Sommet> chaine = graph.parcoursLargeurArrange(graph.getIndexAtName("GRAPHE"), graph.getIndexAtName("CAMION"));
		System.out.println("7) La plus courte chaine entre GRAPHE et CAMION est de "+chaine.get(0).getValeur());
		System.out.println("   Cette chaine est composée de "+chaine.toString());
		System.out.println("");
		List<Sommet> chaine2 = graph.diametre();
		System.out.println("8) Le diametre du graphe est " + chaine2.size());
		System.out.println("   Nous savons que c'est pas ça, mais on essaie !");
		System.out.println("   Cette chaine est composée de "+chaine2.toString());
	}
}







//TODO
class Arc {
	
	private Sommet sommet1;
	private Sommet sommet2;
	
	public Arc(Sommet p_sommet1, Sommet p_sommet2) {
		setSommet1(p_sommet1);
		setSommet2(p_sommet2);
	}
	
	public Sommet getSommet1() {
		return sommet1;
	}

	public void setSommet1(Sommet sommet1) {
		this.sommet1 = sommet1;
	}

	public Sommet getSommet2() {
		return sommet2;
	}

	public void setSommet2(Sommet sommet2) {
		this.sommet2 = sommet2;
	}
}






//TODO
class Graph {

	private String nom;
	private List<Sommet> sommets;
	private List<Arc> arcs;
	
	public Graph(ArrayList<Sommet> p_sommets, ArrayList<Arc> p_arcs) {
		setSommets(p_sommets);
		setArcs(p_arcs);
	}

	public Graph() {
		this(new ArrayList<Sommet>(), new ArrayList<Arc>());
	}
	
	public void lectureFichier(String p_path) {
		
		this.setSommets(new ArrayList<Sommet>());
		this.setArcs(new ArrayList<Arc>());
		
		try{
			InputStream ips = new FileInputStream(p_path); 
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			String nom;
			int cpt;
			
			while ((ligne=br.readLine())!=null){
				nom = ligne;
				Sommet sommetCreer = new Sommet(nom);
				
				for(Sommet sommetCompare : this.getSommets()) {
						cpt = 0;
						for(int i=0;i<6;i++) {
							if(sommetCreer.getNom().charAt(i) != sommetCompare.getNom().charAt(i)) {
								cpt++;
								if(cpt>1)
									break;
							}
						}
						if(cpt == 1) {
							sommetCreer.ajouterVoisin(sommetCompare);
							sommetCompare.ajouterVoisin(sommetCreer);
							this.getArcs().add(new Arc(sommetCreer, sommetCompare));
						}
				}
				this.getSommets().add(sommetCreer);
			}
			br.close(); 
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
	}

	public Sommet getIndexAtName(String nom) {
		for(int i = 0; i < this.getSommets().size(); i++) {
			if(this.getSommets().get(i).getNom().equals(nom))
				return this.getSommets().get(i);
		}
		return null;
	}
	
	public List<Sommet> parcoursLargeur(Sommet depart) {
		List<Sommet> temp = new ArrayList<Sommet>();
		List<Sommet> traiter = new ArrayList<Sommet>();
		
		depart.setMarque(true);
		temp.add(depart);
		
		while (temp.size() > 0) {
			Sommet currentSommet = temp.get(0);
			for(int i = 0; i < currentSommet.getSommetAdjacent().size(); i++) {
				if(!currentSommet.getSommetAdjacent().get(i).isMarque()) {
					currentSommet.getSommetAdjacent().get(i).setMarque(true);
					temp.add(currentSommet.getSommetAdjacent().get(i));
				}
			}
			traiter.add(currentSommet);
			temp.remove(currentSommet);
		}
		return traiter;
	}
	
	public List<Sommet> parcoursLargeurArrange(Sommet depart, Sommet arrivee) {
		List<Sommet> temp = new ArrayList<Sommet>();
		
		for(int i = 0; i < this.getSommets().size(); i++) {
			this.getSommets().get(i).setMarque(false);
			this.getSommets().get(i).setValeur(Integer.MAX_VALUE);
		}
		
		depart.setMarque(true);
		depart.setValeur(0);
		temp.add(depart);
		
		while (temp.size() > 0) {
			Sommet currentSommet = temp.get(0);
			for(int i = 0; i < currentSommet.getSommetAdjacent().size(); i++) {
				if(!currentSommet.getSommetAdjacent().get(i).isMarque()) {
					currentSommet.getSommetAdjacent().get(i).setMarque(true);
					currentSommet.getSommetAdjacent().get(i).setValeur(currentSommet.getValeur()+1);
					temp.add(currentSommet.getSommetAdjacent().get(i));
				}
			}
			temp.remove(currentSommet);
		}
		return getChemin(arrivee);
	}
	
	public List<Sommet> getChemin(Sommet s) {
		List<Sommet> temp = new ArrayList<Sommet>();
		List<Sommet> chemin = new ArrayList<Sommet>();
		int index = s.getValeur();
		temp.add(s);
		while (temp.size() > 0) {
			index--;
			Sommet currentSommet = temp.get(0);
			for(int i = 0; i < currentSommet.getSommetAdjacent().size(); i++) {
				if(currentSommet.getSommetAdjacent().get(i).getValeur() == index) {
					temp.add(currentSommet.getSommetAdjacent().get(i));
					break;
				}
			}
			chemin.add(currentSommet);
			temp.remove(currentSommet);
		}
		return chemin;
	}
	
	public List<Sommet> diametre() {
		Sommet max = this.getSommets().get(0);
		for(int i=1; i < this.getSommets().size(); i++) {
			if(this.getSommets().get(i).getValeur() > max.getValeur()
			&& this.getSommets().get(i).getValeur() != Integer.MAX_VALUE) {
				max = this.getSommets().get(i);
			}
		}
		return getChemin(max);
	}
	
	public List<List<Sommet>> nombreComposantesConnexes() {
		List<List<Sommet>> result = new ArrayList<List<Sommet>>();
		for(int i = 0; i < this.getSommets().size(); i++) {
			this.getSommets().get(i).setMarque(false);
		}
		result.add(this.parcoursLargeur(this.getSommets().get(0)));
		for(int i=1; i < this.getSommets().size(); i++) {
			if(!this.getSommets().get(i).isMarque()) {
				result.add(this.parcoursLargeur(this.getSommets().get(i)));
			}
		}
		return result;
	}
	
	public int nbComposante_N_mots(List<List<Sommet>> connexe, int nbMots) {
		int nb = 0;
		for(int i=0; i < connexe.size(); i++) {
			if(connexe.get(i).size() == nbMots) {
				nb++;
			}
		}
		return nb;
	}
	
	public HashMap<Integer, Integer> nbSommet_K_Voisins() {
		HashMap<Integer,Integer> result = new HashMap<Integer,Integer>(); 
		for(int i=0; i < this.getSommets().size(); i++) {
			if(!result.containsKey(this.getSommets().get(i).getDegres()))
				result.put(this.getSommets().get(i).getDegres(), 1);
			else
				result.put(this.getSommets().get(i).getDegres(), result.get(this.getSommets().get(i).getDegres()) + 1);
			
		}
		return result;
	}
	
	public void ajoutSommet(Sommet p_sommet) {
		getSommets().add(p_sommet);
	}
	
	public void suppressionSommet(Sommet p_sommet) {
		getSommets().remove(p_sommet);
	}
	
	public int nbSommet() {
		return getSommets().size();
	}

	public String toString() {
        return "Graph "+nom;
    }
	
	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public List<Sommet> getSommets() {
		return sommets;
	}

	public void setSommets(List<Sommet> sommets) {
		this.sommets = sommets;
	}

	public List<Arc> getArcs() {
		return arcs;
	}

	public void setArcs(List<Arc> arcs) {
		this.arcs = arcs;
	}
}
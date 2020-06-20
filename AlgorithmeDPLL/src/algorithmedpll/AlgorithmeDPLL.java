package algorithmedpll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

class Proposition{
    char sym;
    int valeur;
    
    public Proposition() {
        this.sym = '\u0000';
        this.valeur = 0;
    }
    public Proposition(char sym, int valeur) {
	this.sym = sym;
	this.valeur = valeur;
    }	
    @Override
    public String toString(){
        String s = "";
            if(this.valeur == 1){
                s+="VRAI";
            }
            if(this.valeur == 0){
                s+="FAUX";
            }
        return Character.toString(sym)+" = "+s;  
    }
}

public class AlgorithmeDPLL{
    public static ArrayList<String> afficherSymbolesSousFormeDeListe(String symboles){
        ArrayList<String> liste = new ArrayList<>();
        for(int i=0; i<symboles.length(); i++){
            liste.add(Character.toString(symboles.charAt(i)));
        }
        return liste;
    }
    public static String  receuillirSym(String enonce) {
        String l = "";
	HashSet hset= new HashSet();
	for(int i=0; i<enonce.length(); i++) {
            if (enonce.charAt(i)!= '(' && enonce.charAt(i) != ')' && enonce.charAt(i)!= '&' && enonce.charAt(i)!= '|' && enonce.charAt(i) != '!') {
		hset.add(enonce.charAt(i));
            }
	}
	Iterator it= hset.iterator();
        ArrayList<String> test = new ArrayList<>();
        while (it.hasNext()) {
            test.add(Character.toString((Character)it.next()));
	}
        Collections.sort(test);
        for(String element : test){
            l += element;
        }
        return l;
    }
    public static HashMap<Integer, String> receuillirClause(String enonce) {
		HashMap <Integer ,String> map= new HashMap<Integer, String>();
		String clause= "";
                int j=1;
                for(int i=0; i<enonce.length(); i++) {
                    if(enonce.charAt(i)!= '(' & enonce.charAt(i) != ')' & enonce.charAt(i)!= '|') {
                        if(enonce.charAt(i) != '&' ) {
                            clause= clause + enonce.charAt(i);
                        }else {
                            map.put(j, clause);
                            j=j+1;
                            clause= "";
                        }
                    }
                    if(i == enonce.length()-1) {
                        map.put(j, clause);
                    }
                }
                return map;
	}
    public static String concatenerClause(ArrayList<String> liste) {
            String valeur="";
            for(String element: liste){
                valeur += element;
            }
            return valeur;
	}
    public static Proposition rechercherSymbPur(String enonce, String symbole ) {
        Proposition p= new Proposition();
        int i=0; 
        boolean flag= false;
        int indfalse=0;
        int indtrue=0;
		
	while (i< symbole.length() && flag == false) { 
            indfalse=0;
            indtrue=0;
            for(int j=0; j<enonce.length(); j++) {
		if (enonce.charAt(j)== symbole.charAt(i)) {
                    if(j>0 && enonce.charAt(j-1)== '!') {
			indfalse += 1;
                    }
                    else {
			indtrue +=1;
                    }
		}
            }
            if (indfalse ==0 || indtrue==0) {
		flag= true;
		}else {
			i+=1;
		}
		
	}
  if(flag == true) {
	  p.sym= symbole.charAt(i);
	  if(indtrue == 0) {
		  p.valeur=0;
	  }else p.valeur=1;
	  return p;
	  }else {
		  p.sym='\u0000';
	  } return p;
  }
    public static boolean testClausefausse(ArrayList<String> clause){
            boolean flag = false;
            int i=0;
            while(!flag && i< clause.size()){
                if((clause.get(i).equals("F"))){
                    flag= true;
                }
                i++;
            }
            return flag;
        
        }
    public static boolean dpll(ArrayList<String> clauses,String symboles,ArrayList<Proposition> modele){
            Proposition p = new Proposition();
            ArrayList<String> liste = new ArrayList<>();           
            if(clauses.isEmpty()){
                System.out.println("Liste des symboles : []");
                System.out.println("Liste des clauses : " + clauses);
                System.out.println("Le modèle est : " + modele);
                System.out.println();
                return true;
            }
            else{
                String val= concatenerClause(clauses);
                //verifie s'il ya une clause fausse dans clauses 
                if(testClausefausse(clauses)){
                    return false;
                }
                else{
                    p = rechercherSymbPur(val, symboles);
                    System.out.println("Liste des symboles : "+afficherSymbolesSousFormeDeListe(symboles));
                    System.out.println("Liste des clauses : " + clauses);
                    System.out.println("Le modèle est : " + modele);
                    System.out.println();
                    if(p.sym!='\u0000'){
                        System.out.println("On supprime le symble pur : "+p.sym);
                    }
                    if(p.sym!='\u0000'){
                        modele.add(p);
                   
                        liste = (ArrayList<String>)(supprimerClauseSachantSymbolePur(clauses,p).clone());
                    
                        val= concatenerClause(liste);
                        symboles = receuillirSym(val);
                        return dpll(liste,symboles,modele);
                    }else{
                       
                        p = chercherClauseUnitaire(clauses);
                        if(p.sym!='\u0000'){
                            String s ="";
                            if(p.valeur == 1){
                                s+=p.sym;
                            }
                            if(p.valeur == 0){
                                s+="!"+p.sym;
                            }
                            System.out.println("On supprime la clause unitaire : "+s);
                            System.out.println("Et on supprime aussi le symbole: <<"+Character.toString(p.sym)+">> de l'ensemble des symboles");
                        }
                        if(p.sym!='\u0000'){
                            modele.add(p);
                            liste = (ArrayList<String>)(supprimerClausesSachantClauseUnitaire(clauses,p).clone());
                            
                            val= concatenerClause(liste);
                            symboles = receuillirSym(val);
                            return dpll(liste,symboles,modele);
                        }else{
                            Proposition p1 = new Proposition();
                            Proposition p2 = new Proposition();
                            p1.sym = symboles.charAt(0);
                            p1.valeur = 1;
                            p2.sym = symboles.charAt(0);
                            p2.valeur = 0;
                            String reste = symboles.substring(1);
                            System.out.println(reste);
                            ArrayList<Proposition> modele1 = new ArrayList<>();
                            modele1 = (ArrayList<Proposition>)(modele.clone());
                            modele1.add(p1);                            
                            ArrayList<Proposition> modele2 = new ArrayList<>();
                            modele2 = (ArrayList<Proposition>)(modele.clone());
                            modele2.add(p2);
                            return dpll(supprimerClausesSachantClauseUnitaire(clauses,p1),reste,modele1) || dpll(supprimerClausesSachantClauseUnitaire(clauses,p2),reste,modele2);
                        }
                    }
                }
            }
        }
    public static ArrayList<String> supprimerClauseSachantSymbolePur(ArrayList<String> clauses, Proposition prop){
            //la proposition prop est un symbole pur, on supprime donc toutes 
            //les clauses qui contiennent cette proposition
            ArrayList<String> liste = new ArrayList<>();
            liste = (ArrayList<String>)(clauses.clone());
            int i = 0;
            int k = 0;
            while (!liste.isEmpty() && i<liste.size()) {
                k = (liste.get(i)).indexOf(Character.toString(prop.sym));
                if(k==-1){
                    i+=1;
                }
                else{
                    liste.remove(i);
                }                
            }
            return liste;
        }
    public static ArrayList<String> supprimerClausesSachantClauseUnitaire(ArrayList<String> clauses, Proposition prop){
            //la proposition prop ici represente la clause unitaire. 
            // donc on trouve d'abord la clause unitaire et on la retire l'ensemble des clauses
            ArrayList<String> liste = new ArrayList<>();
            liste = (ArrayList<String>)(clauses.clone());
            int i = 0;
            while(!liste.isEmpty() && i<liste.size()){
                int k = (liste.get(i)).indexOf(Character.toString(prop.sym));
                if(k== -1){
                    i+=1;
                }
                else{
                    if(k==0){
                        if(prop.valeur == 1){
                            liste.remove(i);
                        }
                        else{
                            if((liste.get(i)).length() ==1){
                                liste.set(i, "F");
                                i += 1;
                            }
                            else{
                                liste.set(i,(liste.get(i)).substring(1));
                                i += 1;
                            }
                        }
                    }
                    else{
                        if((liste.get(i)).charAt(k-1)=='!'){
                            if(prop.valeur == 0){
                                liste.remove(i);
                            }
                            else{
                               if(liste.get(i).length()==2){
                                   liste.set(i,"F");
                                   i++;
                               }
                               else{
                                   String S1= null, S2= null;
                                   if(k!= (liste.get(i).length())-1 && k>1){
                                       S1 = (liste.get(i)).substring(0,k-1);
                                       S2 = (liste.get(i)).substring(k+1);
                                       liste.set(i, S1+S2);
                                       i+=1;
                                    }
                                   else{
                                       if(k!= (liste.get(i).length())-1 && k==1){
                                           liste.set(i,(liste.get(i)).substring(k+1,liste.get(i).length()));
                                           i+=1;
                                        }
                                       else{
                                           if(k == (liste.get(i).length())-1 && k>1){
                                               liste.set(i,(liste.get(i)).substring(0,k-1));
                                               i+=1;
                                        }
                                    }
                                }
                               }
                            }
                        }
                        else{
                            if(prop.valeur==1){
                                liste.remove(i);
                            }
                            else{
                                if(k!=(liste.get(i).length())-1){
                                    String S1= "", S2= "";
                                    S1 = (liste.get(i)).substring(0,k);
                                    S2 = (liste.get(i)).substring(k+1);
                                    liste.set(i, S1+S2);
                                    i +=1;
                                }
                                else{
                                    String S1= null;
                                    S1 = (liste.get(i)).substring(0,k);
                                    liste.set(i,S1);
                                    i +=1;
                                }
                            }
                        }
                    }
                }
            } 
            return liste;
    }
    public static Proposition chercherClauseUnitaire(ArrayList<String> liste){
            Proposition p = new Proposition();
            for(String element: liste){
                if(element.length() == 1){
                    p.sym = element.charAt(0);
                    p.valeur = 1;
                    break;
                 }
                if(element.length()==2){
                    if(element.charAt(0)=='!'){
                        p.sym = element.charAt(1);
                        p.valeur = 0;
                        break;
                    }
                }
            }
            return p;
        }
    public static String charRemoveAt(String str, int p) {  
        return str.substring(0, p) + str.substring(p + 1);  
    }
    
	
    public static void main(String[] args) {
	//(p|k)&(!l|t)&(!p|j)&(t)
        // (!A)&(!B|C|D)&(!C|B)&(!D|B)&(!E|A|G|H)&(!A|E)&(!G|E)&(!H|E)&(!B)&(E)&(C)
        Proposition p=new Proposition();
        Proposition p1=new Proposition();
        p1.sym = 'V';
        p1.valeur = 1;
        Proposition p2=new Proposition();
        p2.sym = 'F';
        p2.valeur = 0;
        
	Scanner sc= new Scanner(System.in);
        System.out.println("Lorsque vous entrez un enonce, notre algorithme retroune : ");
        System.out.println("\t true si l'enoncé EST SATISFIABLE ");
        System.out.println("\t false si l'enoncé N'EST PAS SATISFIABLE");
	System.out.println();
        System.out.println("Entrez un nouvel enonce");
	String enonce= sc.nextLine();
        
        String symboles = receuillirSym(enonce);
        
        ArrayList<Proposition> modele = new ArrayList<>();
	HashMap <Integer ,String> map= receuillirClause(enonce);
        
        ArrayList<String> nouvelEnonce = new ArrayList();
        for(String element: map.values()){
            nouvelEnonce.add(element);
        }
        ArrayList<String> fakeEnonce = new ArrayList<>();
        fakeEnonce = (ArrayList<String>)supprimerClausesSachantClauseUnitaire(nouvelEnonce, p1);
        fakeEnonce = (ArrayList<String>)supprimerClausesSachantClauseUnitaire(fakeEnonce, p2);
        
        nouvelEnonce = (ArrayList<String>)fakeEnonce.clone();
        
        String val = concatenerClause(nouvelEnonce);
        
       System.out.println();
       System.out.println("************ Deroulement de l'agorithme **************");
       System.out.println(dpll(nouvelEnonce, symboles, modele));
	}     
}                  
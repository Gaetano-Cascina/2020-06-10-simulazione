package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Graph<Actor, DefaultWeightedEdge> graph;
	private Map<Integer, Actor> idMap;
	
	public Model() {
		dao = new ImdbDAO();
		idMap = new HashMap<>();
		
	}
	
	public void creaGrafo(String genere) {
		
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		dao.listAtt(idMap, genere);
		
		Graphs.addAllVertices(this.graph, idMap.values());
		
		List<Arco> archi = dao.getArchi(genere);
		
		for(Arco a : archi) {
			Graphs.addEdge(this.graph, idMap.get(a.getId1()), idMap.get(a.getId2()), a.getPeso());
		}
		
		
		
	}
	
	public List<Actor> attori(){
		List<Actor> att = new ArrayList<>();
		
		for(Actor a : this.graph.vertexSet()) {
			att.add(a);
		}
		
		Collections.sort(att, new Comparator<Actor>() {

			@Override
			public int compare(Actor o1, Actor o2) {
				return o1.lastName.compareTo(o2.lastName);
			}
		});
		
		return att;
	}
	
	public List<String> generi(){
		return dao.listGeneri();
	}
	
	public int getNumVertex() {
		return this.graph.vertexSet().size();
	}
	
	public int getNumArchi() {
		return this.graph.edgeSet().size();
	}
	
	public List<Actor> attoriSimili(Actor a){
		
		ConnectivityInspector<Actor, DefaultWeightedEdge> ci = new ConnectivityInspector<Actor, DefaultWeightedEdge>(graph);
		List<Actor> actors = new ArrayList<>(ci.connectedSetOf(a));
		actors.remove(a);
		Collections.sort(actors, new Comparator<Actor>() {

			@Override
			public int compare(Actor o1, Actor o2) {
				return o1.lastName.compareTo(o2.lastName);
			}
			
		});
		return actors;
		
	}


}

# Un exemple minimaliste d'une application Jakarta EE 8 (ou Java EE 8)

Pour faire simple l'application consiste à manipuler des comptes bancaires (très simplifiés).

Un compte est identifié par un numéro (alphanumérique) et le montant du compte.

On peut :
- créer des comptes,
- débiter ou créditer un compte et
- transférer de l'argent d'un compte vers un autre.

Les comptes sont stockés dans une base de données. Une entité (JPA) permet d'y accéder (cf. partie EJBs).

Un EJB session sans état (Stateless) sert de façade pour les opérations bancaires.

Un client WEB permet de réaliser l'ensemble des opérations sur les comptes : création, débit, crédit, transfert, visualisation ou recherche (cf. client Web). 
Ce client comporte deux pages jsp et plusieurs servlet (1 par opération). La connexion au bean session se fait à l'aide de son interface distante.

## Partie EJB

L'objet persistant, Compte, ainsi que l'EJB session et son interface sont regroupés ensembles dans la même archive.
<pre>
CompteEjb.jar (ejb-jar)
  |-- <a href="CompteEjb/src/main/java/fr/usmb/m2isc/javaee/comptes/jpa/Compte.java" >fr/usmb/m2isc/.../jpa/Compte.class</a> (implantation de l'entité Compte (entité JPA))
  |-- <a href="CompteEjb/src/main/java/fr/usmb/m2isc/javaee/comptes/ejb/OperationBean.java" >fr/usmb/m2isc/.../ejb/OperationBean.class</a> (implantation du l'EJB Operation (bean session))
  |-- <a href="CompteEjb/src/main/java/fr/usmb/m2isc/javaee/comptes/ejb/Operation.java" >fr/usmb/m2isc/.../ejb/Operation.class</a> (interfaces de manipulation distante du bean session)
  |-- META-INF/MANIFEST.MF (java manifeste)
  |-- <a href="CompteEjb/src/main/resources/META-INF/ejb-jar.xml" >META-INF/ejb-jar.xml</a> (descripteur standard des Jakarta Enterprise Beans (EJB) -- optionnel dans les dernières versions de javaEE et pour Jakarta EE)
  |-- <a href="CompteEjb/src/main/resources/META-INF/persistence.xml" >META-INF/persistence.xml</a> (descripteur standard pour JPA)
  |-- META-INF/orm.xml (descripteur pour le mapping objet-relationnel -- absent ici)
</pre>

Toutes les manipulations sur les objets persistants se font dans l'EJB en utilisant l'*entity manager* correspondant à l'*unité de persistance* des objets persistants manipulés. 

Dans l'EJB on utilise l'annotation `@PersistenceContext` pour récupérer auprès du serveur Jakarta EE (ou Java EE) l'*entity manager* désiré.

```java
@Stateless
@Remote
public class OperationBean implements Operation {
	
	@PersistenceContext
	private EntityManager em;
```

Pour les opérations de recherche de comptes, sur l'entité JPA ont été ajoutés deux requetes nommées

```java
@NamedQueries ({
	@NamedQuery(name="all", query="SELECT c FROM Compte c"),
	@NamedQuery(name="findWithNum", query="SELECT c FROM Compte c WHERE c.numero LIKE :partialNum ORDER BY c.numero ASC")
})
@Entity
public class Compte implements Serializable {
	...
```

utilisées dans l'EJB session pour obtenir soit la liste complete des comptes, soit une partie d'entre eux :

```
@Stateless
@Remote
public class OperationBean implements Operation {	
	...	
	@Override
	public List<Compte> findAllComptes() {
		Query req = em.createNamedQuery("all");
		return req.getResultList();
	}

	@Override
	public List<Compte> findComptes(String partialNumber) {
		Query req = em.createNamedQuery("findWithNum");
		req.setParameter("partialNum", partialNumber);
		return req.getResultList();
	}	
	...
```

 
## L'application WEB est dans un fichier d'archive war :

Ce client WEB permet de réaliser l'ensemble des opérations sur les comptes : création, débit, crédit, transfert, recherche ou visualisation.

Ce client comporte des pages jsp et des servlet. La connexion au bean session se fait à l'aide de l'interface distante de l'EJB. 
On utilise les *servlet* pour traiter les requêtes et les *pages JSP* pour l'affichage du résultat.
<pre>
CompteWeb.war
  |-- <a href="CompteWeb/src/main/webapp/index.html" >index.html</a> (page d'accueil -- formulaires html permettant de créer, rechercher ou modifier les comptes)
  |-- <a href="CompteWeb/src/main/webapp/AfficherCompte.jsp" >AfficherCompte.jsp</a> (page jsp pour afficher un compte)
  |-- <a href="CompteWeb/src/main/webapp/AfficherCompte.jsp" >AfficherComptes.jsp</a> (page jsp pour afficher plusieurs comptes)
  |-- <a href="CompteWeb/src/main/webapp/META-INF/MANIFEST.MF" >META-INF/MANIFEST.MF</a> (java manifeste)
  |-- WEB-INF/classes (classes java pour les servlets :
                |-- <a href="CompteWeb/src/main/java/fr/usmb/m2isc/javaee/comptes/web/CreerCompteServlet.java" >fr/usmb/m2isc/javaee/comptes/web/CreerCompteServlet.class</a>
                |-- <a href="CompteWeb/src/main/java/fr/usmb/m2isc/javaee/comptes/web/CrediterCompteServlet.java" >fr/usmb/m2isc/javaee/comptes/web/CrediterCompteServlet.class</a>
                |-- <a href="CompteWeb/src/main/java/fr/usmb/m2isc/javaee/comptes/web/DebiterCompteServlet.java" >fr/usmb/m2isc/javaee/comptes/web/DebiterCompteServlet.class</a>
                |-- <a href="CompteWeb/src/main/java/fr/usmb/m2isc/javaee/comptes/web/TransfererServlet.java" >fr/usmb/m2isc/javaee/comptes/web/TransfererServlet.class</a>
                |-- <a href="CompteWeb/src/main/java/fr/usmb/m2isc/javaee/comptes/web/ChercherComptesServlet.java" >fr/usmb/m2isc/javaee/comptes/web/ChercherComptesServlet.class</a>
                |-- <a href="CompteWeb/src/main/java/fr/usmb/m2isc/javaee/comptes/web/AfficherCompteServlet.java" >fr/usmb/m2isc/javaee/comptes/web/AfficherCompteServlet.class</a>
  |-- WEB-INF/lib (librairies java utilisées dans les servlet)
  |-- <a href="CompteWeb/src/main/webapp/WEB-INF/web.xml" >WEB-INF/web.xml</a> (descripteur standard de l'application Web -- optionnel dans les dernières versions de javaEE et pour Jakarta EE)
</pre>

Dans les *servlet* on utilise l'annotation `@EJB` pour obtenir une référence de l'*EJB session* :

```java
@WebServlet("/CreerCompteServlet")
public class CreerCompteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private Operation ejb;
```

L'EJB est ensuite utilisé par les servlet pour effectuer les traitements :

```java
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// recuperation des parametres de la requete
		String num = request.getParameter("numero");
		String val = request.getParameter("depot");
		double depot = Double.parseDouble(val);
		
		// utilisation de l'EJB
		Compte cpt = ejb.creerCompte(num, depot);
		...
```

puis on utilise la requête pour passer les objets à afficher à la *page JSP* chargée de l'affichage :

```java
		...
		// ajout du compte dans la requete
		request.setAttribute("compte", cpt);
		// redirection vers la page JSP pour afficher le compte
		request.getRequestDispatcher("/AfficherCompte.jsp").forward(request, response);		
	}
```

## Le tout est packagé ensemble dans une archive ear :

Cette archive permet de regrouper dans le même fichier l'ensemble des composants de l'application (ejb, app web, etc.).
<pre>
CompteEar.ear
  |-- CompteEjb.jar (archive contenant les EJBs)
  |-- CompteWeb.war (archive contenant le client Web)
  |-- <a href="CompteEar/src/main/application/META-INF/application.xml" >META-INF/application.xml</a> (descripteur standard de l'application -- optionnel dans les dernières versions de javaEE)
</pre>

## Usage :

Pour voir les sources il suffit de cloner le projet git et de l'importer (sous forme de projet gradle) dans votre IDE favori. 
Cela devrait permettre la création de 3 sous-projets (ou modules), un pour la partie EJB et JPA , un pour la partie WEB et un pour la partie EAR.

La création des archives (CompteWeb.war, CompteEjb.jar, CompteEar.ear) peut se faire via gradle en appelant la tâche build sur le projet principal.

Pour utiliser l'exemple il suffit de déployer le fichier CompteEar.ear sur un serveur Jakarta EE 8 (ou Java EE 8). 
Le client Web est alors dans déployé dans */CompteWeb*.

## Documentation :

Java EE 7 (Oracle)
- Doc : http://docs.oracle.com/javaee/7
- Tutoriel : https://docs.oracle.com/javaee/7/tutorial
- API (javadoc) : http://docs.oracle.com/javaee/7/api
- Spécifications : https://www.oracle.com/java/technologies/javaee/javaeetechnologies.html#javaee7

Jave EE 8 (Oracle)
- Doc : https://javaee.github.io/glassfish/documentation
- Tutoriel : https://javaee.github.io/tutorial/
- API (javadoc) : https://javaee.github.io/javaee-spec/javadocs/
- Spécifications : https://www.oracle.com/java/technologies/javaee/javaeetechnologies.html#javaee8
- Serveurs compatibles : https://www.oracle.com/java/technologies/compatibility-jsp.html

Jakarta EE 8 (Fondation Eclipse)
- Doc : https://javaee.github.io/glassfish/documentation
- Tutoriel : https://javaee.github.io/tutorial/
- API (javadoc) : https://jakarta.ee/specifications/platform/8/apidocs/
- Spécifications : https://jakarta.ee/specifications
- Serveurs compatibles : https://jakarta.ee/compatibility/certification/8/

Jakarta EE 9 (Fondation Eclipse)
- Doc : https://jakarta.ee/resources/#documentation
- Tutoriel : https://eclipse-ee4j.github.io/jakartaee-tutorial/
- API (javadoc) : https://jakarta.ee/specifications/platform/9/apidocs/
- Spécifications : https://jakarta.ee/specifications
- Serveurs compatibles : 
    - https://jakarta.ee/compatibility/certification/9/
    - https://jakarta.ee/compatibility/certification/9.1/

Jakarta EE 9 (Fondation Eclipse)
- Doc : https://jakarta.ee/resources/#documentation
- API (javadoc) : https://jakarta.ee/specifications/platform/10/apidocs/
- Spécifications : https://jakarta.ee/specifications
- Serveurs compatibles : 
    - https://jakarta.ee/compatibility/certification/10/



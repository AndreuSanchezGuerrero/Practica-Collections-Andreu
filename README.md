# Introducció.

La següent pràctica tracta de crear un **mercat online**, on es poden **comprar productes d'alimentació, electrònica i tèxtils**. El programa té un menú principal on tens 4 possibles opcions:

     1. Intorduir producte.
     2. Passar per caixa.
     3. Mostrar el que tenim a la cistella.
     4. Omplir el carretó de productes random
     5. Mostra preu carret.
     6. Sortir

**Informació general del programa.**

- El programa ens dóna els bons dies, la bona tarda o la bona nit depenent de l'hora que sigui.
- Introduir producte ens permet afegir un producte d'una de les categories dites anteriorment.
- Passar per caixa ens ensenya el rebut, per posteriorment pagar. També ens dóna l'opció d'escollir si en volem còpia o no.
- Mostrar carretó ens mostra els productes que tenim afegits al carretó.
- Omplir carretó, ens permet omplir el carretó amb objectes random (per facilitar la feina al professor). PD: Els preus no van a cord amb el producte, hi pot haver un pa de 70€.
- Mostrar preu carretó, ens informa del preu que tenim actualment al carretó, per no passar-nos i que després no puguem pagar.
- La darrera opció és sortir.

Important:
     - El carretó no pot superar els 100 productes.
     - La longitud d'un nom no pot superar els 15 caràcters.
     - Els productes amb un codi de barres igual que algun del fitxer updates, s'actualitzarà. Això ho tenim per si tenim dies amb ofertes per exemple un black friday.

## .gitignore

- He fet un .gitignore per no pujar al repositori els fitxers que crea el IDE (fitxers .iml).
- El propi java incorpora un .gitignore per no pujar res del out.

**gitignore general, per excloure els fitxers IML**
![gitignoreIML](./images_per_fer_readme/gitignoreIML.png)

**gitignore dintre del projecte per excloure el out**
![gitignoreOUT](./images_per_fer_readme/gitignoreOUT.png)

## Classe abstracte 'Producte'

- La classe és abstracta per no poder crear productes amb la classe 'Producte', només en podrem crear subclasses de la mateixa.

- Podrem modificar (setters) tot excepte el codi de barres, per això el posem com a variable constant ja que no variarà. En el meu cas, crec que codi de barres sempre serà el mateix.

- Totes les variables tindran els seus getters, ja que els necesitem.

- **Important: he fet la variable 'quantitat' que s'anirà actualitzant a mesura que anem afegint productes amb el mateix codi de barres**

- 'Calcular preu' -> Mètode abstracte perquè sigui obligatori anomenar-lo a les seves subclasses.

## Subclasse de Producte 'Alimentació'

- Demanem la data en format dd-mm-yyyy (String).

- 'calcularPreu()' -> Calculem el preu segons la data de caducitat. 
    1. Guardem la data actual.
    2. Convertim la data de caducitat de String a LocalDate amb format(dd-MM-yyy) per poder operar amb ella.
    3. Fem l'operació que ens diu l'enunciat, pero en compte de 'preu - preu*(1/(dataCaducitat-dataActual+1)) **Aquí va una suma en comptes de una resta perquè no cuadren els resultats** (preu*0.1)'.

- El getter de preu serà el resultat del mètode 'calcularPreu()'.

- No tenim cap setter, ja que el preu que es té que poder modificar és el de la classe pare 'Producte' que es el preu base, aquí es necessita el preu fent el càlcul. La data de caducitat tampoc ha de poder cambiarse, no te sentit que es pugui modificar. La fem com ha variable constant.

**calcularpreu()**

  ````java
   // Sobreescrivim el mètode obstracte de Producte
  // Sobreescrivim el mètode obstracte de Producte
    @Override
    public float calcularPreu() {
        // Obtenim la data actual 
        LocalDate dataActual = LocalDate.now();

        // Guardem la DATACADUCITAT en format LocalDate i la parsegem al format dd-mm-yyyy.
        LocalDate dataParsejada = LocalDate.parse(DATACADUCITAT, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        // Operació de l'enunciat
        int resta = dataActual.until(dataParsejada).getDays() +1;
        float preuFinal = super.getPreu() - super.getPreu() * (1.0f / resta) + (super.getPreu() * 0.1f);
        return preuFinal;

        @Override
        public float getPreu() {
            return calcularPreu();
        }
    }
  ````

  - Ordenem per nom amb l'interface comarable.
  
  ````java
    @Override
    public int compareTo(Alimentacio a1) {
        return this.getNom().compareTo(a1.getNom());
    }
  ````

## Suclasse de Producte 'Textil'
 
- La variable composicioTextil serà un enum, per fer ja directament el control d'errors i no haber de fer una llista amb els tipus de composició tèxtil.

````java 
// Enum de composició tèxtil
public enum enumCompositioTextil {COTO, POLIESTER, LLI, SEDA, LLANA, NILO;};
    enumCompositioTextil composicioTextil;
````

- Sobreescrivim el mètode calcularpreu(), però deixiem els valors de la classe super (Producte) perquè no varia en aquest cas.
  
````java
   // Sobreescrivim el mètode abstracte de Producte i el deixem igual, no varia de la classe superior
    @Override
    public float calcularPreu() {
        return super.getPreu();
    }

    @Override
    public float getPreu() {
        return calcularPreu();
    }

````
- Composició tèxtil tindrà el seu setter per poder cambiarla.

- Orndenem els productes per composició textil, per aixo farem servir l'interface compareTo().
  ````java
   @Override
    public int compareTo(Textil t1) {
        // String ja implementa l'interface compareTo i la fem servir.
        return this.getComposicioTextil().compareTo(t1.getComposicioTextil());
    }
  ````
    
## Suclasse de Producte 'Electronica'

- En aquest cas data de garantia si que es podrá modificar. Perquè es posible que al producte li retallin la vida de garantia o extendre-la.

- Sobreescrivim el mètode calcularpreu() i fem l'operacio de l'enunciat. El preu d'aquest tipus de producte varia en funció dels dies que té de garantia.

````java

    // Sobreescrivim el mètode abstracte de Producte i fem l'operacio de l'enunciat
    @Override
    public float calcularPreu() {
        // Fem l'operació de l'enunciat
        float operacioGarantia = super.getPreu() + super.getPreu()*(diesGarantia/365)*0.1f;
        return operacioGarantia;
    }

    @Override
    public float getPreu() {
        return calcularPreu();
    }  
````

- Ordenem els productes de electronica per nom amb l'inteface comparable.
  
  ````java
      @Override
    public int compareTo(Electronica e1) {
        return this.getNom().compareTo(e1.getNom());
    }
  ````
  
## Classe CarroCompra

- Classe important on definirem tots els mètodes i funcions amb els quals omplirem el main, de forma que en el main només tinguem les opcions a escollir.

- **En aquest context no necessitem operacions específiques de LinkedList, com ara la inserció o eliminació enmig de la llista, i atès que accedirem als elements de manera seqüencial, ArrayList sembla l'opció més adequada. A més, ArrayList ofereix un accés més ràpid als elements mitjançant índexs.**
  
**<u>Variables</u>**

- Llista on tindrem l'informació de tots els productes.

````java
        // Llista de productes
    protected static ArrayList<Producte> llistaProductes;
````

- Llista de productes en la que no farem cas a la quantitat i afegirem tots els productes com a independents, així podem veure la mesura de la llista i si ja té 100 productes afegits. De no fer aquesta copia, no calcularà bé si tenim 100 productes, o hariem de fer un for amb la llista de productes principal i calcular la quantitat. És més eficient fer un arraylist ja que 100 productes no son masses.

````java
        // Llista de productes de control
    protected static ArrayList<Producte> llistaProductesCopia;
````

- Tindrem una variable per controlar el limit de productes de una manera més intuitiva.

````java
    // Variable constant que limita el nombre de productes
    static int LIMIT_PRODUCTES = 100;
````

- Hashmap per controlar que no hi hagin més téxtils 

- generarCodiDeBarres(String nom) -> Mètode per crear aleatoriament el codi de barres fent servir el hasmap nomYCodigsProductes.

````java

    // Generem el codi de barres de forma aleatoria
    public String generarCodiDeBarres(String nom) {
        // Guardem en aquesta variable un numero aleatori entre 100 i 999 perquè aixi tenim un codi de barres de 3 digits
        int numeroAleatori = random.nextInt(100,999);

        // Concatenem el nom del producte amb el numero aleatori
        String codiDeBarresFinal = nom + "-" + String.valueOf(numeroAleatori);
        return codiDeBarresFinal;
    }
    
````

- afegirProducte(Alimentacio Producte) -> Mètode per afegir un producte a la llista de productes i al mapa de productes que el necessitarem per fer un recorregut del carro.
      1. Afegim el producte a la llista de productes, ho necessitarem per calcular el preu de tot el carret.
      2. Afegim el producte al diccionari de productes, ho necessitarem més endevant per veure els productes del carro i quants tenim amb el mateix codi de barres.
  
````java

    public void afegirProducte(Alimentacio producte) {
        
        // 
        llistaProductes.add(producte);

        //
        if (mapProductes.containsKey(producte.getCODI_DE_BARRES())) {
            // Si el producte ja existeix, augmentar la quantitat +1.
            int quantitat = mapProductes.get(producte.getCODI_DE_BARRES());
            mapProductes.put(producte.getCODI_DE_BARRES(), quantitat + 1);
        } else {
            // Si el producte no existeix, afegir-lo amb una quantitat de 1
            mapProductes.put(producte.getCODI_DE_BARRES(), 1);
        }
        System.out.println("S'ha afegit " + producte.getNom() + " amb codi de barres: " + producte.getCODI_DE_BARRES() + '\n');
    }
````

- Mètode saludar()
  
    1. La aplicació ens dirà 'bon dia', 'bona tarda' o 'bonanit' en funció de l'hora que sigui.
    2. La formula es la seguent: (Si es després de les 06:00h i abans de les 14:00h) direm bon dia, (si es després de les 14:00h abans de les 20.00h) direm bona tarda y (si es després de les 20:00h i abans de les 06:00) direm bona nit. 

````java
    public String saludar() {
        // Obtenir l'hora actual
        LocalTime horaActual = LocalTime.now();

        // Saludar en funció de l'hora
        String saludar;
        if (horaActual.isBefore(LocalTime.of(14, 0)) && horaActual.isAfter(LocalTime.of(06, 0))) {
            saludar = "Bon dia";
        } else if (horaActual.isBefore(LocalTime.of(20, 0))&& horaActual.isAfter(LocalTime.of(14, 0))) {
            saludar = "Bona tarda";
        } else {
            saludar = "Bona nit";
        }
        return saludar;
    }
````

- menu1() -> Mostrem les opcions que podem escollir. Aquesta funció s'executarà des de el main.

````java
    // Metode per mostrar el menu principal
    public void menu1() {
       // Guardem la mida de la String saludar per que quedi nivellat amb els guions
        String saludar = "---" + saludar() + " benvingut al mercat online---";
        // Fem que saludar pasi a ser una llista amb el '.split'. Amb el bucle 'for' fem el recorregut de la llista i per cada recorregut, afegim un guió a la variable guions perquè aixì queda simetric.
        String guions = "";
        for (String guion:saludar.split("")) {
            guions += "-";
        }
        System.out.println(guions);
        System.out.println(saludar);
        System.out.println(guions);
        System.out.println("(1) Introduir producte");
        System.out.println("(2) Passar per caixa");
        System.out.println("(3) Mostar carret de compra");
        System.out.println("(0) Acabar");
    }
````

- En cas d'introduir '1' al menu1(), s'executarà el menu2() amb opcions a escollir.

- Expressió regular per validar la data.
    1. '^' indica el principi de la cadena.
    2. '(0[1-9]|[1-2][0-9]|3[0-1])' Permet valors de 01 a 09, 10 a 29, 30 y 31 que es el dia.
    3. '-' Separació per guió.
    4. '(0[1-9]|1[0-2])' Permet valors de 01 a 09 (Gener) a 12 (Decembre).
    5. '-' Separació per guió.
    6. '(202[5-9]|20[3-9][0-9])' Permet valors de 2025 hasta 2099.
    7. '$'.

````java
// Expressió regular per validar la data
    private boolean comprovarDataCaducitat(String data) {
        Pattern patron = Pattern.compile("^(0[1-9]|[1-2][0-9]|3[0-1])-(0[1-9]|1[0-2])-(202[5-9]|20[3-9][0-9])$");
        Matcher mat = patron.matcher(data);
        return mat.matches();
    }
````

- afegirProducte(Alimentacio producte) -> Mètode que fem servir per afegir un producte al diccionari de productes i a la llista de productes.

````java
  public void afegirProducte(Alimentacio producte) {
        // Afegim el producte a la llista de productes generals per calcular el preu
        llistaProductes.add(producte);

        // Afegim el producte al diccionari de productes per veure els productes del carro i quants tenim
        if (mapProductes.containsKey(producte.getCODI_DE_BARRES())) {
            // Si el producte ja existeix, augmentar la quantitat +1.
            int quantitat = mapProductes.get(producte.getCODI_DE_BARRES());
            mapProductes.put(producte.getCODI_DE_BARRES(), quantitat + 1);
        } else {
            // Si el producte no existeix, afegir-lo amb una quantitat de 1
            mapProductes.put(producte.getCODI_DE_BARRES(), 1);
        }
        System.out.println("S'ha afegit " + producte.getNom() + " amb codi de barres: " + producte.getCODI_DE_BARRES() + '\n');
    }
````

- crearProducte() -> Creació de producte i dintre cridem a afegirproducte(producte)

````java
    public void crearProducte() {
        //
        String nom;
        float preu;
        String codiDeBarres;
        String dataCaducitat;
        //
        System.out.println("Introdueix el nom del producte: ");
        nom = input.nextLine();
        System.out.println("Introdueix el preu del producte: ");
        preu = input.nextFloat();

        // Comprovem si el nom ja ha sortit previament.
        // Si no ha sortit generem un codi de barres.
        // Si ja ha sortit, agafim el codi de barres ja generat previament per aquest nom.
        if (nomYCodigsProductes.containsKey(nom)) {
            codiDeBarres = nomYCodigsProductes.get(nom);
        } else {
            codiDeBarres= generarCodiDeBarres(nom);
            nomYCodigsProductes.put(nom, codiDeBarres);
        }

        // Comprovem la data de caducitat i si no es correcta tornem a provar. No es un error tan greu com per llançar una excepció
        System.out.println("Introdueix la data de caducitat del producte: ");
        dataCaducitat = input.nextLine();
        while (!comprovarDataCaducitat(dataCaducitat)) {
            System.out.println();
            System.out.println("Data incorrecta, torna-ho a provar.");
            System.out.println("Introdueix la data en format (dd-mm-yyyy): ");
            dataCaducitat = input.nextLine();
        }
        
        // Creem el producte i l'afegim a la llista de productes i al diccionari de productes
        Alimentacio producte = new Alimentacio(nom, preu, codiDeBarres, dataCaducitat);
        afegirProducte(producte);
        System.out.println("Producte "+nom+ " afegit correctament");
    }
````


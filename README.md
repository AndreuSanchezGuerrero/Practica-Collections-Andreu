# Explicació de la práctica

## Classe abstracte 'Producte'

- La classe és abstracta per no poder crear productes, només en podrem crear subclasses de la mateixa.

- Podrem modificar tot excepte el codi de barres, per això el posem com a variable constant ja que no variarà. En el meu cas, crec que codi de barres sempre serà el mateix.

- Totes tindran els seus getters.

- 'Calcular preu' -> Mètode abstracte perquè sigui obligatori anomenar-lo a les seves subclasses.

## Subclasse de producte 'Alimentació'

- Demanem la data en format dd-mm-yyyy. A la classe CarroCompra comprovarem la data controlant-la per regex i tornan a donar l'oportunitat d'introduir la data.

- Calculem el preu segons la data de caducitat amb el métode 'calcularPreu()'. 
    1. Guardem la data actual.
    2. Convertim la data de caducitat de String a LocalDate amb format(dd-MM-yyy) per poder operar amb ella.
    3. Fem l'operació que ens diu l'enunciat, pero en compte de 'preu - preu*(1/(dataCaducitat-dataActual+1)) **Aquí va una suma en comptes de una resta perquè no cuadren els resultats** (preu*0.1)'.

- El getter de preu serà el resultat del mètode 'calcularPreu()'.

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

## Suclasse de producte 'Textil'

- Sobreescrivim el mètode calcularpreu(), però deixiem els valors de la classe super.
  
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
  
## Suclasse de producte 'Electronica'

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

- En el main ja comprovarem si l'usuari no ha posat un Integer amb l'error InputMismatchException.
  
## Classe CarroCompra

- Classe important on definirem tots els mètodes i funcions amb els quals omplirem el main, de forma que en el main només tinguem les opcions a escollir.

- **En aquest context no necessitem operacions específiques de LinkedList, com ara la inserció o eliminació enmig de la llista, i atès que accedirem als elements de manera seqüencial, ArrayList sembla l'opció més adequada. A més, ArrayList ofereix un accés més ràpid als elements mitjançant índexs. Per això calcularem el preu amb arraylist.**
  
````java
    Scanner input = new Scanner(System.in);
    // Dades globals
    // Llista de productes per calcular el preu
    private ArrayList<Producte> llistaProductes;

    // Diccionari per veure els productes del carro
    private HashMap<String, Integer> mapProductes;

    //Diccionari que farem servir per crear un codi de barres aleatori
    private static HashMap<String, String> nomYCodigsProductes;
    private static Random random;

    //-----------------------------------------------------------------------------
    // Constructor
    public CarroCompra() {
        nomYCodigsProductes= new HashMap<>();
        random = new Random();
        mapProductes = new HashMap<>();
        llistaProductes = new ArrayList<Producte>();
    }
    //-----------------------------------------------------------------------------
````

-**Farem dos hasmap. Un per crear aleatoriament el codi de barres i un per mostrar el carret**

-Mètode per crear aleatoriament el codi de barres fent servir el hasmap nomYCodigsProductes

````java

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
- Mètode menu1()
    1. Fem que ens surtin els guions a nivell amb la String de saludar. Fem que saludar pasi a ser una llista amb el '.split'.
    2. Mostrem les opcions que podem escollir.

- En cas d'introduir '1' al menu1(), s'executarà el menu2() amb opcions a escollir. 

- Expressió regular per validar la data.
    1. '^' indica el principi de la cadena.
    2. '(0[1-9]|[1-2][0-9]|3[0-1])' Permet valors de 01 a 09, 10 a 29, 30 y 31 que es el dia.
    3. '-' Separació per guió.
    4. '(0[1-9]|1[0-2])' Permet valors de 01 a 09 (Gener) a 12 (Decembre).
    5. '-' Separació per guió.
    6. '(202[5-9]|20[3-9][0-9])' Permet valors de 2025 hasta 2099.
    7. '$'.
   
 
# Explicació de la práctica.
### Classe abstracte 'Producte'.
- La classe és abstracta per no poder crear productes, només en podrem crear subclasses de la mateixa.
- Podrem modificar tot excepte el codi de barres, per això el posem com a variable global ja que no variarà.
- Totes tindran els seus getters.
- Tindrem un mètode abstracte de 'calcular preu' perquè sigui obligatori anomenar-lo a les seves subclasses.

### Subclasse de producte 'Alimentació'.
- Demanem la data en format dd-mm-yyyy i fem control de errors amb regex per comprovar-ho.
- Calculem el preu segons la data de caducitat amb el métode 'calcularPreu()'. 
    1. Guardem la data actual.
    2. Convertim la data de caducitat de String a LocalDate amb format(dd-MM-yyy) per poder operar amb ella.
    3. Fem l'operació que ens diu l'enunciat, pero en compte de 'preu - preu*(1/(dataCaducitat-dataActual+1)) **Aquí va una suma en comptes de una resta perquè no cuadren els resultats** (preu * 0.1)'.
- El getter de preu serà el resultat del mètode 'calcularPreu()'.
- Expressió regular per validar la data.
    1. '^' indica el principi de la cadena.
    2. '(0[1-9]|[1-2][0-9]|3[0-1])' Permet valors de 01 a 09, 10 a 29, 30 y 31 que es el dia.
    3. '-' Separació per guió.
    4. '(0[1-9]|1[0-2])' Permet valors de 01 a 09 (Gener) a 12 (Decembre).
    5. '-' Separació per guió.
    6. '(202[5-9]|20[3-9][0-9])' Permet valors de 2025 hasta 2099.
    7. '$'.

### Suclasse de producte 'Textil'
- Sobreescrivim el mètode calcularpreu(), però deixiem els valors de la classe super.   
  

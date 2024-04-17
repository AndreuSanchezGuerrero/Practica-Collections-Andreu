# Explicació de la práctica.


### Classe abstracte producte
- La classe és abstracta per no poder crear productes, només en podrem crear subclasses de la mateixa.
- Podrem modificar tot excepte el codi de barres, per això el posem com a variable global ja que no variarà.
- Totes tindran els seus getters.
- Tindrem un mètode abstracte de 'calcular preu' perquè sigui obligatori anomenar-lo a les seves subclasses.

### Subclasse de producte 'Alimentació'.
- Demanem la data en format dd-mm-yyyy i fem control de errors amb regex per comprovar-ho.
- Calculem el preu segons la data de caducitat amb el métode 'calcularPreu()'. 
    1. Guardem la data actual.
    2. Convertim la data de caducitat de String a LocalDate per poder operar amb ella.
    3. Fem la operació que ens diu l'enunciat.
- El getter de precio será el resultado del metodo descrito
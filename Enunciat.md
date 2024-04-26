# Enunciat

***També ha de permetre que, en passar per caixa, es generi el tiquet de compra i es buidi el carro.***

            El tiquet (es mostra per pantalla) ha de mostrar una capçalera amb: data de la compra i nom del supermercat. A continuació es mostra el detall amb: nom del producte, unitats introduïdes al carro, preu unitari i preu total. Finalment ha de calcular la suma total a pagar.

            Si s'han introduït dos productes iguals (tenen el mateix codi de barres i el mateix preu unitari) només es mostrarà una vegada, amb la quantitat total d'aquell producte, és a dir, les unitats.

            Aquesta opció també implica buidar el carro de la compra.

## Part de collections

·         Cal implementar la interfície Comparable amb el seu corresponent mètode en una classe que considereu que només cal fer una ordenació natural, i per tant, ens cal també implementar en una altra classe la interfície Comparator amb el seu mètode corresponent definit per vosaltres i que ens permeti comparar objectes de diferent manera a l’estàndard.

## Sobre les Excepcions

1.- Ens demanen abans de “Passar per caixa” que comprovem en un repositori (considerarem com a repositori la nostra màquina en local) dins de la carpeta .\updates un fitxer de nom UpdateTextilPrices.dat, que conté uns preus actualitzats sobre certes peces de tèxtil (inventeu-vos el contingut del fitxer per a poder fer les proves que us calguin). Caldrà comprovar segons codi de barres si el producte que trobem al fitxer es troba al carret, en aquest cas s’haurà d’actualitzar el carret. Però la feina important rau en que heu de fer un control exhaustiu de les possibles excepcions i errors que us poden aparèixer en executar aquesta opció.

2.-  Per a qualsevol dels productes que ens demanen, cal fer control sobre les excepcions i/o errors en els següents supòsits:

2.1- Controlarem la llargada dels productes que es volen introduir al Carret de la Compra, per a qualsevol dels 3 tipus de productes que es poden introduir al carret, la seva llargada màxima del nom serà de 15 caràcters.

2.2.- Controlarem el tipus de dades esperat, és a dir, segons l’entrada que hagi per teclat haurem de fer una gestió dels possibles errors i/o excepcions.

3.- Ens demanen guardar totes les excepcions que es produeixen en un fitxer dintre de la carpeta .\logs:

                               .\updates\UpdateTextilPrices.dat  (de l’exercici 1 d’exepcions)

                               .\logs\Exceptions.dat

## Extra Andreu

- Passar un fitxer i omplir dades.
- Preguntar si vol copia. 
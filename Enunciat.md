# Enunciat
La multinacional SAPAMERCAT us demana que li dissenyeu una aplicació revolucionaria! Consisteix en fer que el carro de la compra mostri, en temps real, el preu dels productes que s'hi van introduint.

L'empresa us indica que l'aplicació, de moment, només ha de permetre gestionar les dades d'uns quants dels seus productes: alimentació, tèxtil i electrònica. Aquests productes tenen unes característiques comuns (preu, nom i codi de barres) i un conjunt de característiques específiques de cada tipus de producte:

    Alimentació: data de caducitat.

        El preu d'aquest tipus de producte varia en funció dels dies que falten per caducar, segons la fórmula:

             preu - preu*(1/(dataCaducitat-dataActual+1)) + (preu * 0.1)

    Tèxtil: composició tèxtil (text)

    Electrònica: dies de garantia (numèric)

        El preu d'aquest tipus de producte varia en funció dels dies que té de garantia segons la fórmula:

         preu + preu*(diesGarantia/365)*0.1

L'aplicació que heu de fer ha de permetre emmagatzemar tots els productes que s'hi van introduint (màxim 100 productes) i calcular-ne el preu. 

També ha de permetre que, en passar per caixa, es generi el tiquet de compra i es buidi el carro.

Fer un programa principal que faci ús de les classes dissenyades. La descripció del que ha de fer aquest programa s'explica a continuació. El programa ha de tenir un menú d'opcions com el següent:

        1. Introduir producte En escollir aquesta opció s'ha de mostrar un altre menú d'opcions: Quin tipus de producte vols afegir?

            1.1. Alimentació En escollir aquesta opció s'ha de demanar que s'entri per teclat les dades d'un producte del tipus Alimentació

            1.2. Tèxtil En escollir aquesta opció s'ha de demanar que s'entri per teclat les dades d'un producte del tipus Tèxtil

            1.3. Electrònica En escollir aquesta opció s'ha de demanar que s'entri per teclat les dades d'un producte del tipus Electrònica

            1.0. Tornar En escollir aquesta opció s'ha de tornar al menú principal

        2. Passar per caixa En escollir aquesta opció se simula que es passen tots els productes per caixa i es genera el tiquet.

            El tiquet (es mostra per pantalla) ha de mostrar una capçalera amb: data de la compra i nom del supermercat. A continuació es mostra el detall amb: nom del producte, unitats introduïdes al carro, preu unitari i preu total. Finalment ha de calcular la suma total a pagar.

            Si s'han introduït dos productes iguals (tenen el mateix codi de barres i el mateix preu unitari) només es mostrarà una vegada, amb la quantitat total d'aquell producte, és a dir, les unitats.

            Aquesta opció també implica buidar el carro de la compra.

        3. Mostrar carro de la compra En escollir aquesta opció es mostra un llistat amb la descripció i quantitat de cada producte (sense preu) que hi ha dins el carro del a compra. En aquest cas, si hi ha productes repetits ho seran si tenen el mateix codi de barres (no cal mirar el preu unitari).

        0. Sortir En escollir aquesta opció es tanca l'aplicació.

## Exemples
![Exemple 1](Exemple1.png)
![Exemple 2](Exemple2.png)
![Exemple 3](Exemple3.png)
![Exemple 4](Exemple4.png)

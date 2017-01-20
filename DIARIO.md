# Diario del trabajo
En este documento se irá recogiendo el avance del trabajo por días, con las observaciones y conclusiones que se vayan realizando. La finalidad de esto es facilitar la redacción de una memoria al finalizar el trabajo.


## 13/12/2016
Nos acaban de explicar la práctica, aqúi recojo los comentarios que he apuntado:

- Hay que implementar las 3 variables: estandar, variable lamarkiana y variable balwiniana:
  - Las 2 ultimas utilizan optimización local en cada cromosoma y utiliza como valor de fitness el optimizado.


- El problema del QAP es una generalización del viajante de comercio.


- La idea de la variantes lamarkianas y baldwinianas es que podemos utilizar un algoritmo de optimización local (greddy eficiente) para mejorar la solución que representa ese cromosoma
  - Lamarkiana => se hereda lo aprendido => los cromosomas heredados son los optimizados.
  - Baldwin => el cromosoma sigue siendo el original, no se heredan los rasgos optimizados pero es mas probable que hereden.


- En la pagina de la base de datos están las soluciones óptimas conocidas, lo suyo es acercarse a eso.


- Modelo elitista para mecanismo de remplazo de generación: los mejores individuos pasan a la siguiente generación directamente, no son eliminados al cambiar.


- Operador de mutuación: si solución es vector int no basta con modificar 1 valor, lo mas habitual es intercambiar 2 valores al azar del vector.


- Operador de cruce: distintos tipos


- Mecanismo de seleción de individuos a reproducir


- Una permutación es valida si no se repiten los números (es un vector de ints)


- Función de coste dependiente 2 matrices, distancias y flujos


- Echarle un vistazo a las [diapositivas de la asignatura sobre computación evolutiva (G1 y G2)](http://elvex.ugr.es/decsai/computational-intelligence/):

```
Evolución	 <=>	Resolución de problemas
Entorno       <=>	 Problema
Individuo 	<=>	Solución candidata
Adaptación    <=>	Calidad de la solución
 (fitness)
```

#### Caraterísticas esenciales de un algoritmo genético
- Inicialización (generación aleatoria de una población inicial)
- Variación (operadores de cruce y mutación)
- Evaluación (aptitud [fitness] de cada individuo)
- Selección (selección probabilística)

#### Pseudocódigo de un algoritmo genético clásico

![imagen pseudocódigo](Imgs/Pseudocodigo básico Alg Gene.png "imagen pseudocódigo")

## 17/01/2017
He creado un proyecto en eclipse con una clase `Data` que contiene la funcionalidad necesaria para cargar los distintos ficheros de datos que se encuentran en la carpeta `qap.data`.

Para comenzar con la implementación se puede ver [http://www.theprojectspot.com/tutorial-post/creating-a-genetic-algorithm-for-beginners/3](http://www.theprojectspot.com/tutorial-post/creating-a-genetic-algorithm-for-beginners/3) que parece un tutorial básico e interesante para crear una versión inicial del algoritmo.

#### Campos a rellenar del formulario de entrega de resultados
- Algoritmo empleado (estandar, variante baldwiniana, variante lamarckiana...)
- Tamaño población
- Número generaciones
- Mecanismo de selección (de padres)
  - Selección proporcional - Ruleta
  - Selección proporcional - SUS [Stochastic Universal Sampling]
  - Selección proporcional con "windowing"
  - Selección proporcional con "sigma scaling"
  - Ranking lineal
  - Ranking exponencial
  - Selección por torneo
  - otro
- Mecanismo de remplazo (selección de supervivientes)
  - Modelo generacional (Standard GA)
  - Modelo estacionario (Steady-state GA)
  - Modelo elitista
  - otro
- Operador de cruce
- Operador de mutación
- Optimización local
- Coste de la solución => en que unidades??
- Solución encontrada

Me pondré a leer la presentación G2 de los apuntes de la asignatura para entender que significa cada una de estas cosas y esta tarde en clase empezaré más con la implementación.

#### Preguntas iniciales
- [x] **¿Cuál sería el genotipo de este problema, que representaría?** El genotipo es el orden en el que se multiplican las columnas de una de las matrices (la de distancia por ejemplo)
- [x] **¿Cuál será la función de fitness?** El valor de la función de optimización en negativo.
- [x] **¿Qué es la permutación p() sobre el conjunto de instalaciones?** Es un vector de enteros del tamaño del problema concreto en los que no se puede repetir ningún elemento.  
- [ ] **¿Cual será el operador de cruce?**
- [ ] **¿Cuál será el mecanismo de selección?**
- [ ] **¿Cuál será el operador de mutación?**

#### Comentarios de clase
Hay que minimzar la sumatoria de A[i][j] * B [P(i)][P(j)]

Para el operador de cruce hay que elegir por donde cortar, una vez elegido eso se combina pero probando que al introducir un número no esté ya, si ya está se seleccionaría el siguiente. Ej.
```
12|34 => 12|43
31|24 => 31|42
```

## 19/01/2017

Leyendo el libro de [*Genetic Algorithms in Java Basics*](http://www.apress.com/9781484203293)  by Lee Jacobson and Burak Kanber (Apress, 2015), en él resuelven el problema del viajante, que tiene características similares al QAP, por ello he decidido leer como lo solucionan para aplicar medidas similares.

- He visto que seria interesante como operador de cruce el crossover ordenado.
- Como condición de terminación usare un número máximo de generaciones sin mejorar el fitness medio de la población. Habrá que mirar como funciona porque igual es mejor hacerlo sobre el fitness del mejor individuo ya que el fitness medio se puede incrementar lentamente y no terminar casi nunca aunque se atasque.

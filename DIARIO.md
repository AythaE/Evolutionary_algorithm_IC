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

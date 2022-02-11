# epidemic-model-simulation
Epidemic model simulation at the AGH University of Science and Technology.

## Table of Contents
* [General info](#general-info)
* [Technologies](#technologies)

## General info
* The purpose of this project is to develop and implement an epidemic development model with the use of cellular automata. 
* For the purposes of the study, the AGH University of Science and Technology in Krakow, which is a specific form of enterprise, particularly exposed to the virus, was chosen as the simulation area. 
* An important aspect of the work is the assumption of taking into account the interaction of individuals and not a certain population of them. 
* In order to reflect the specificity of the local environment as closely as possible, it was decided to define repetitive routines simulating the working day in the function responsible for the movement of cells. 
* The coefficients of the proposed transition function are modified by the model depending on the location of the cells. 
* The implemented graphical user interface allows modifying simulation parameters and monitoring its progress. 
* The simulation scenarios carried out investigate the influence of personal protective measures and vaccination on the epidemic dynamics.

## Technologies
The program is created with:
* Java 17
* JavaFX 11.0.2
* Spring Boot 2.4.4

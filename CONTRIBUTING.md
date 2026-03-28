# Contribuir a PlaceNote-Client

Gracias por tu interés en contribuir a PlaceNote.

## Contrato API

Los cambios que afecten a peticiones o modelos compartidos deben alinearse con **[PlaceNote-Server](https://github.com/AlvaroMinarro/PlaceNote-Server)** y su `docs/api/openapi.yaml`. Coordina cambios breaking entre ambos repositorios (issues enlazados).

## Flujo sugerido

1. Fork y rama desde `main`.
2. `./gradlew :shared:assemble` en local antes de abrir un PR.
3. Describe plataformas probadas (Android / iOS / ambas).

## Estilo

- Código y APIs públicas en inglés, salvo convención explícita del proyecto.
- Commits con mensajes claros.

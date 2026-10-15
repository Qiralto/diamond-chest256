# Diamond Chest 256 — PROJET SOURCE NON COMPILÉ

**Statut réel :** code source rédigé, pas compilé ni testé en jeu. Ce ZIP n'est **pas** un mod installable. L'API exacte Minecraft 26.3 et les versions Gradle/Fabric restent à valider. Ne stockez pas d'objets précieux avant un test en monde de sauvegarde.

## Contenu
- Bloc `coffrediamant256:diamond_chest`, recette de 8 diamants + 1 coffre.
- Block entity Java `Container` de **256** cases avec sauvegarde NBT et drop des objets à la casse.
- Menu basé sur l'interface vanilla 9x6 : 5 pages de 52 cases maximum ; dernière page avec 48 cases. Flèches aux cases 53/54 pour naviguer. Attention : les slots de navigation et la gestion des clics n'ont pas été testés.
- Texture simple 16x16 bleue. Le bloc utilise un modèle cubique : **ce n'est pas encore un coffre 3D à couvercle animé**.
- Accès à l'inventaire via `net.minecraft.world.Container`, prévu pour les systèmes d'inventaire ; **compatibilité Tom's non testée**, et Fabric Transfer API peut exiger une intégration supplémentaire.
- GitHub Actions pour tenter de compiler.

## Compilation GitHub (sans installer Java)
1. Sur GitHub, créer un dépôt **privé** ou public, par exemple `diamond-chest-256`.
2. Décompresser le ZIP et envoyer **le contenu du dossier** `diamond_chest_256` à la racine du dépôt, en incluant `.github/workflows/build.yml`.
3. Aller dans l'onglet **Actions** et lancer `Compile Diamond Chest 256` si nécessaire.
4. Si l'action échoue, copier le journal de l'étape `Compile Fabric mod` et le transmettre pour correction. **C'est possible et probable à cette étape**.
5. Si elle réussit, télécharger l'artifact `diamond-chest-256-jar`, puis vérifier le `.jar` avant toute installation.

## Points encore à valider / corriger
- Compatibilité des signatures API Minecraft 26.3, notamment BlockEntity NBT, menu, item registration, interaction et la recette.
- Cohérence des versions Fabric Loom, Fabric API, Gradle et Java 25.
- Menu paginé : affichage, boutons de navigation, clics rapides, synchronisation et slots inaccessibles de dernière page.
- Hoppers, connecteurs Tom's Simple Storage, Crafting Terminal, insertion/extraction depuis le réseau.
- Tests de sauvegarde, crash, duplication, multijoueur, cassage avec 256 piles et inventaire plein.
- Modèle 3D animé et couvercle (non implémenté).

Le succès de la compilation n'est pas une preuve de fonctionnement correct : testez d'abord dans un monde de copie.

### Envy-Freeness Scenario

![envy_freeness](envy_freeness.png)

These are some of the implemented fairness tiles for the scenario:

| Index | Tile or Pipeline                                 | Class                                        |
|:------|:-------------------------------------------------|:---------------------------------------------|
| 1     | all-agent <sub>*(a)*</sub>                       | [AllAgentTile][AllAgentTile]                 |
| 2     | all-resource <sub>*(r)*</sub>                    | [AllResourceTile][AllResourceTile]           |
| 3     | <sub>*(α),(β)*</sub> cross <sub>*(⟨α, β⟩)*</sub> | [CrossTile][CrossTile]                       |
| 4     | <sub>*(α)*</sub> filter ϕ <sub>*(α)*</sub>       | [FilterTile][FilterTile]                     |
| 5     | <sub>*(α)*</sub> map ϕ <sub>*(β)*</sub>          | [MapTile][MapTile]                           |
| 6     | <sub>*(α)*</sub> exists ϕ <sub>*b*</sub>         | [ExistsTile][ExistsTile]                     |
| 7     | composite (3 + 4 + 5)                            | [CrossFilterMapTile][CrossFilterMapTile]     |
| 8     | composite (3 + 4)                                | [CrossFilterTile][CrossFilterTile]           |
| 9     | composite (3 + 6)                                | [CrossExistsTile][CrossExistsTile]           |
| 10    | pipeline (7 + 8 + 9)                             | [EnvyFreenessPipeline][EnvyFreenessPipeline] |

[AllAgentTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/constant/AllAgentTile.soda

[AllResourceTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/constant/AllResourceTile.soda

[CrossTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/primitive/CrossTile.soda

[FilterTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/primitive/FilterTile.soda

[MapTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/primitive/MapTile.soda

[ExistsTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/composite/ExistsTile.soda

[CrossFilterMapTile]: https://github.com/julianmendez/tiles/blob/master/examples/src/main/scala/soda/tiles/fairness/example/pipeline/envyfreeness/CrossFilterMapTile.soda

[CrossFilterTile]: https://github.com/julianmendez/tiles/blob/master/examples/src/main/scala/soda/tiles/fairness/example/pipeline/envyfreeness/CrossFilterTile.soda

[CrossExistsTile]: https://github.com/julianmendez/tiles/blob/master/examples/src/main/scala/soda/tiles/fairness/example/pipeline/envyfreeness/CrossExistsTile.soda

[EnvyFreenessPipeline]: https://github.com/julianmendez/tiles/blob/master/examples/src/main/scala/soda/tiles/fairness/example/pipeline/envyfreeness/EnvyFreenessPipeline.soda



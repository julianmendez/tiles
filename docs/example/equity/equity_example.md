### Equity Scenario

![equity0](equity0.png)

These are some of the implemented fairness tiles for the scenario:

| Index | Tile or Pipeline                              | Class                                              |
|:------|:----------------------------------------------|:---------------------------------------------------|
| 1     | all-agent <sub>*(a)*</sub>                    | [AllAgentTile][AllAgentTile]                       |
| 2     | <sub>*(a)*</sub> accumulates <sub>*(m)*</sub> | [AccumulatesTile][AccumulatesTile]                 |
| 3     | <sub>*(a)*</sub> needs <sub>*(m)*</sub>       | [NeedsTile][NeedsTile]                             |
| 4     | <sub>*(m)*</sub> all-at-least <sub>*b*</sub>  | [AllAtLeastTile][AllAtLeastTile]                   |
| 5     | composite (1 + 2)                             | [AllAgentAccumulatesTile][AllAgentAccumulatesTile] |
| 6     | composite (1 + 3)                             | [AllAgentNeedsTile][AllAgentNeedsTile]             |
| 7     | pipeline (5 + 6 + 4)                          | [EquityPipeline][EquityPipeline]                   |

Equity can also be modeled as:

![equity1](equity1.png)

| Index | Tile or Pipeline                               | Class                              |
|:------|:-----------------------------------------------|:-----------------------------------|
| 1     | all-agent <sub>*(a)*</sub>                     | [AllAgentTile][AllAgentTile]       |
| 2     | <sub>*(a)*</sub> accumulates <sub>*(m)*</sub>  | [AccumulatesTile][AccumulatesTile] |
| 3     | <sub>*(a)*</sub> needs <sub>*(m)*</sub>        | [NeedsTile][NeedsTile]             |
| 4     | <sub>*(α),(β)*</sub> zip <sub>*(⟨α, β⟩)*</sub> | [ZipTile][ZipTile]                 |
| 5     | <sub>*(α)*</sub> forall ϕ <sub>*b*</sub>       | [ForallTile][ForallTile]           |
| 6     | composite (1 + 2)                              |                                    |
| 7     | composite (1 + 3)                              |                                    |
| 8     | composite (4 + 5)                              |                                    |
| 9     | pipeline (6 + 7 + 8)                           |                                    |

[AllAgentTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/constant/AllAgentTile.soda

[AccumulatesTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/composite/AccumulatesTile.soda

[NeedsTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/derived/map/NeedsTile.soda

[AllAtLeastTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/composite/AllAtLeastTile.soda

[ZipTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/primitive/ZipTile.soda

[ForallTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/composite/ForallTile.soda

[AllAgentAccumulatesTile]: https://github.com/julianmendez/tiles/blob/master/examples/src/main/scala/soda/tiles/fairness/example/pipeline/equity/AllAgentAccumulatesTile.soda

[AllAgentNeedsTile]: https://github.com/julianmendez/tiles/blob/master/examples/src/main/scala/soda/tiles/fairness/example/pipeline/equity/AllAgentNeedsTile.soda

[EquityPipeline]: https://github.com/julianmendez/tiles/blob/master/examples/src/main/scala/soda/tiles/fairness/example/pipeline/equity/EquityPipeline.soda



### Jain's Index Scenario

![jains_index](jains_index.png)

These are some of the implemented fairness tiles for the scenario:

| Index | Tile or Pipeline                              | Class                                                              |
|:------|:----------------------------------------------|:-------------------------------------------------------------------|
| 1     | all-agent <sub>*(a)*</sub>                    | [AllAgentTile][AllAgentTile]                                       |
| 2     | <sub>*(a)*</sub> accumulates <sub>*(m)*</sub> | [AccumulatesTile][AccumulatesTile]                                 |
| 3     | <sub>*(m)*</sub> sum <sub>*m*</sub>           | [SumTile][SumTile]                                                 |
| 4     | <sub>*(α)*</sub> map ϕ <sub>*(β)*</sub>       | [MapTile][MapTile]                                                 |
| 5     | <sub>*(α)*</sub> length <sub>*m*</sub>        | [LengthTile][LengthTile]                                           |
| 6     | <sub>*α*</sub> apply ϕ <sub>*β*</sub>         | [ApplyTile][ApplyTile]                                             |
| 7     | composite (1 + 2 + 3 + 6)                     | [AllAgentAccumulatesMapSumTile][AllAgentAccumulatesMapSumTile]     |
| 8     | composite (1 + 2 + 4 + 3)                     | [AllAgentAccumulatesSumApplyTile][AllAgentAccumulatesSumApplyTile] |
| 9     | composite (1 + 5)                             | [AllAgentLengthTile][AllAgentLengthTile]                           |
| 10    | pipeline (6 + 7 + 8 + 9)                      | [JainsIndexPipeline][JainsIndexPipeline]                           |

[AllAgentTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/constant/AllAgentTile.soda

[AccumulatesTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/composite/AccumulatesTile.soda

[SumTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/derived/fold/SumTile.soda

[MapTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/primitive/MapTile.soda

[LengthTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/derived/fold/LengthTile.soda

[ApplyTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/primitive/ApplyTile.soda

[AllAgentAccumulatesMapSumTile]: https://github.com/julianmendez/tiles/blob/master/examples/src/main/scala/soda/tiles/fairness/example/pipeline/jainsindex/AllAgentAccumulatesMapSumTile.soda

[AllAgentAccumulatesSumApplyTile]: https://github.com/julianmendez/tiles/blob/master/examples/src/main/scala/soda/tiles/fairness/example/pipeline/jainsindex/AllAgentAccumulatesSumApplyTile.soda

[AllAgentLengthTile]: https://github.com/julianmendez/tiles/blob/master/examples/src/main/scala/soda/tiles/fairness/example/pipeline/jainsindex/AllAgentLengthTile.soda

[JainsIndexPipeline]: https://github.com/julianmendez/tiles/blob/master/examples/src/main/scala/soda/tiles/fairness/example/pipeline/jainsindex/JainsIndexPipeline.soda



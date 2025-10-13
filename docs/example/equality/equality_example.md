### Equality Scenario

![equality0](equality0.png)

These are some of the implemented fairness tiles for the scenario:

| Index | Tile or Pipeline                              | Class                                |
|:------|:----------------------------------------------|:-------------------------------------|
| 1     | all-agent <sub>*(a)*</sub>                    | [AllAgentTile][AllAgentTile]         |
| 2     | <sub>*(a)*</sub> accumulates <sub>*(m)*</sub> | [AccumulatesTile][AccumulatesTile]   |
| 3     | <sub>*(m)*</sub> all-equal <sub>*b*</sub>     | [AllEqualTile][AllEqualTile]         |
| 4     | composite (1 + 2 + 3)                         | [EqualityPipeline][EqualityPipeline] |

Equality can also be modeled as:

![equality1](equality1.png)

| Index | Tile or Pipeline                              | Class                              |
|:------|:----------------------------------------------|:-----------------------------------|
| 1     | all-agent <sub>*(a)*</sub>                    | [AllAgentTile][AllAgentTile]       |
| 2     | <sub>*(a)*</sub> accumulates <sub>*(m)*</sub> | [AccumulatesTile][AccumulatesTile] |
| 3     | <sub>*(m)*</sub> distinct <sub>*(m)*</sub>    | [DistinctTile][DistinctTile]       |
| 4     | <sub>*(α)*</sub> length <sub>*m*</sub>        | [LengthTile][LengthTile]           |
| 5     | <sub>*α*</sub> apply <sub>*β*</sub>           | [ApplyTile][ApplyTile]             |
| 6     | composite (1 + 2 + 3 + 4 + 5)                 |                                    |

[AllAgentTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/constant/AllAgentTile.soda

[AccumulatesTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/composite/AccumulatesTile.soda

[AllEqualTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/composite/AllEqualTile.soda

[DistinctTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/primitive/DistinctTile.soda

[LengthTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/derived/fold/LengthTile.soda

[ApplyTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/primitive/ApplyTile.soda

[EqualityPipeline]: https://github.com/julianmendez/tiles/blob/master/examples/src/main/scala/soda/tiles/fairness/example/pipeline/equality/EqualityPipeline.soda



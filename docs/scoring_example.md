<head>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/mermaid/9.4.3/mermaid.min.js"> </script>
</head>


### Scoring Scenario

These are some of the implemented fairness tiles for scoring scenarios:

| Tile                                               | Class                                        |
|:---------------------------------------------------|:---------------------------------------------|
| all-actor <sub>*(a0), (a1), (a2)*</sub>            | [AllActorTripleTile][AllActorTripleTile]     |
| <sub>*(a)*</sub> prediction <sub>*(m)*</sub>       | [PredictionPTile][PredictionPTile]           |
| <sub>*(a)*</sub> result <sub>*(m)*</sub>           | [ResultPTile][ResultPTile]                   |
| <sub>*(m0), (m1)*</sub> false-pos <sub>*(m)*</sub> | [FalsePosTile][FalsePosTile]                 |
| <sub>*(a)*</sub> with <sub>*(m)*</sub>             | [WithPTile][WithPTile]                       |
| <sub>*(m0), (m1)*</sub> correlation <sub>*m*</sub> | [CorrelationTile][CorrelationTile]           |
| <sub>*m*</sub> decision <sub>*b*</sub>             | [DecisionTile][DecisionTile]                 |
| unbiasedness <sub>*b*</sub>                        | [UnbiasednessPipeline][UnbiasednessPipeline] |

A specific scenario is given as an example in
[ScoringScenarioExample][ScoringScenarioExample]. This scenario is used to test the
scoring scenario tile (unbiasedness with respect to falsepositives) with
[UnbiasednessPipelineSpec][UnbiasednessPipelineSpec].


#### Unbiasedness

```mermaid
graph LR
  all-actor(all-actor) --> prediction
  all-actor --> result
  all-actor --> with-p
  prediction(prediction) --> false-pos
  result(result) --> false-pos
  with-p(with) --> correlation
  false-pos(false-pos) --> correlation
  correlation(correlation) --> decision(decision)
```

[AllActorTripleTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/AllActorTripleTile.soda
[PredictionPTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/PredictionPTile.soda
[ResultPTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/ResultPTile.soda
[FalsePosTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/FalsePosTile.soda
[WithPTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/WithPTile.soda
[CorrelationTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/CorrelationTile.soda
[DecisionTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/DecisionTile.soda
[UnbiasednessPipeline]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/pipeline/UnbiasednessPipeline.soda
[ScoringScenarioExample]: https://github.com/julianmendez/tiles/blob/master/core/src/test/scala/soda/tiles/fairness/pipeline/ScoringScenarioExample.soda
[UnbiasednessPipelineSpec]: https://github.com/julianmendez/tiles/blob/master/core/src/test/scala/soda/tiles/fairness/pipeline/UnbiasednessPipelineSpec.soda

<script>
  window.mermaid.init(undefined, document.querySelectorAll('.language-mermaid'));
</script>



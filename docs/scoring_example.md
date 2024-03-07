<head>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/mermaid/9.4.3/mermaid.min.js"> </script>
</head>


### Scoring Scenario

These are some of the implemented fairness tiles for scoring scenarios:

| Tile                                               | Class                                          |
|:---------------------------------------------------|:-----------------------------------------------|
| all-actor <sub>*(a0), (a1), (a2)*</sub>            | [AllActorTripleTile][all-actor-triple-tile]    |
| <sub>*(a)*</sub> prediction <sub>*(m)*</sub>       | [PredictionPTile][prediction-p-tile]           |
| <sub>*(a)*</sub> result <sub>*(m)*</sub>           | [ResultPTile][result-p-tile]                   |
| <sub>*(m0), (m1)*</sub> false-pos <sub>*(m)*</sub> | [FalsePosTile][false-pos-tile]                 |
| <sub>*(a)*</sub> with <sub>*(m)*</sub>             | [WithPTile][with-p-tile]                       |
| <sub>*(m0), (m1)*</sub> correlation <sub>*m*</sub> | [CorrelationTile][correlation-tile]            |
| <sub>*m*</sub> decision <sub>*b*</sub>             | [DecisionTile][decision-tile]                  |
| unbiasedness <sub>*b*</sub>                        | [UnbiasednessPipeline][unbiassedness-pipeline] |

A specific scenario is given as an example in
[ScoringScenarioExample][scoring-scenario-example]. This scenario is used to test the
scoring scenario tile (unbiasedness with respect to falsepositives) with
[UnbiasednessPipelineSpec][unbiassedness-pipeline-spec].


#### Unbiasedness

```mermaid
graph LR
  all-actor(all-actor) --> prediction
  all-actor --> result
  all-actor --> with
  prediction(prediction) --> false-pos
  result(result) --> false-pos
  with(with) --> correlation
  false-pos(false-pos) --> correlation
  correlation(correlation) --> decision(decision)
```

[all-actor-triple-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/AllActorTripleTile.soda
[prediction-p-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/PredictionPTile.soda
[result-p-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/ResultPTile.soda
[false-pos-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/FalsePosTile.soda
[with-p-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/WithPTile.soda
[correlation-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/CorrelationTile.soda
[decision-tile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/DecisionTile.soda
[unbiassedness-pipeline]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/pipeline/UnbiasednessPipeline.soda
[scoring-scenario-example]: https://github.com/julianmendez/tiles/blob/master/core/src/test/scala/soda/tiles/fairness/pipeline/ScoringScenarioExample.soda
[unbiassedness-pipeline-spec]: https://github.com/julianmendez/tiles/blob/master/core/src/test/scala/soda/tiles/fairness/pipeline/UnbiasednessPipelineSpec.soda

<script>
  window.mermaid.init(undefined, document.querySelectorAll('.language-mermaid'));
</script>



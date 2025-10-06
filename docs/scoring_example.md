<head>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/mermaid/9.4.3/mermaid.min.js"> </script>
</head>


### Scoring Scenario

These are some of the implemented fairness tiles for scoring scenarios:

| Tile                                               | Class                                        | Formerly  |
|:---------------------------------------------------|:---------------------------------------------|:----------|
| all-agent <sub>*(a0), (a1), (a2)*</sub>            | [AllAgentTripleTile][AllAgentTripleTile]     | all-actor |
| <sub>*(a)*</sub> prediction <sub>*(m)*</sub>       | [PredictionPTile][PredictionPTile]           |           |
| <sub>*(a)*</sub> result <sub>*(m)*</sub>           | [MapTile][MapTile]                           |           |
| <sub>*(m0), (m1)*</sub> false-pos <sub>*(m)*</sub> | [FalsePosTile][FalsePosTile]                 |           |
| <sub>*(a)*</sub> with <sub>*(m)*</sub>             | [MapTile][MapTile]                           |           |
| <sub>*(m0), (m1)*</sub> correlation <sub>*m*</sub> | [CorrelationTile][CorrelationTile]           |           |
| <sub>*m*</sub> decision <sub>*b*</sub>             | [DecisionTile][DecisionTile]                 |           |
| unbiasedness <sub>*b*</sub>                        | [UnbiasednessPipeline][UnbiasednessPipeline] |           |

A specific scenario is given as an example in
[ScoringScenarioExample][ScoringScenarioExample]. This scenario is used to test the
scoring scenario tile (unbiasedness with respect to falsepositives) with
[UnbiasednessPipelineSpec][UnbiasednessPipelineSpec].


#### Unbiasedness

```mermaid
graph LR
  all-agent(all-agent) --> prediction
  all-agent --> result
  all-agent --> with-p
  prediction(prediction) --> false-pos
  result(result) --> false-pos
  with-p(with) --> correlation
  false-pos(false-pos) --> correlation
  correlation(correlation) --> decision(decision)
```

[AllAgentTripleTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/constant/AllAgentTripleTile.soda

[PredictionPTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/composite/PredictionPTile.soda

[MapTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/primitive/MapTile.soda

[FalsePosTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/composite/FalsePosTile.soda

[CorrelationTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/composite/CorrelationTile.soda

[DecisionTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/derived/apply/DecisionTile.soda

[UnbiasednessPipeline]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/pipeline/UnbiasednessPipeline.soda

[ScoringScenarioExample]: https://github.com/julianmendez/tiles/blob/master/core/src/test/scala/soda/tiles/fairness/pipeline/ScoringScenarioExample.soda

[UnbiasednessPipelineSpec]: https://github.com/julianmendez/tiles/blob/master/core/src/test/scala/soda/tiles/fairness/pipeline/UnbiasednessPipelineSpec.soda

<script>
  window.mermaid.init(undefined, document.querySelectorAll('.language-mermaid'));
</script>



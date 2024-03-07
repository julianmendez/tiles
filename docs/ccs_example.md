<head>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/mermaid/9.4.3/mermaid.min.js"> </script>
</head>


## Child Care Subsidy Example

| Tile                                                | Class                                    |
|:----------------------------------------------------|:-----------------------------------------|
| all-actor <sub>*(a)*</sub>                          | [AllActorTile][AllActorTile]             |
| <sub>*(a)*</sub> received <sub>*(m)*</sub>          | [ReceivedSigmaPTile][ReceivedSigmaPTile] |
| <sub>*(m)*</sub> all-satisfy (p) <sub>*b*</sub>     | [AllSatisfyPTile][AllSatisfyPTile]       |
| <sub>*(m)*</sub> all-equal <sub>*b*</sub>           | [AllEqualTile][AllEqualTile]             |
| <sub>*(a)*</sub> p <sub>*(m)*</sub>                 | [AttributePTile][AttributePTile]         |
| <sub>*(a)*</sub> p ? <sub>*(a)*</sub>               | [FilterActorTile][FilterActorTile]       |
| <sub>*(m0), (m1)*</sub> all-at-least <sub>*b*</sub> | [AllAtLeastTile][AllAtLeastTile]         |
| <sub>*(m0), (m1)*</sub> f (m0,m1) <sub>*(m)*</sub>  | [SigmaTile][SigmaTile]                   |
| <sub>*b0, b1*</sub> f (b0,b1) <sub>*b*</sub>  | [CombineBooleanTile][CombineBooleanTile]          |


### No Child Care Subsidy

This is the [No Subsidy Pipeline][CcsNoSubsidyPipeline].

```mermaid
graph LR
  all-actor(all-actor) --> received
  received(received) --> all-satisfy-0(all-satisfy m=0)
```


### Child Care Subsidy Per Child Pipeline

This is the [Per Child Pipeline][CcsPerChildPipeline].

```mermaid
graph LR
  all-actor(all-actor) --> received
  all-actor --> children
  received(received) --> m0/m1
  children(children) --> m0/m1
  m0/m1(m0 / m1) --> all-equal(all-equal)
```


### Child Care Subsidy Per Family Pipeline

This is the [Per Family Pipeline][CcsPerFamilyPipeline].

```mermaid
graph LR
  all-actor(all-actor) --> received
  received(received) --> all-equal(all-equal)
```


### Child Card Subsidy to Single Guardian Pipeline

This is the [Single Guardian Pipeline][CcsSingleGuardianPipeline].

```mermaid
graph LR
  all-actor(all-actor) --> adults-1
  all-actor --> adults-2
  adults-1(adults a0 = 1?) --> received-1
  adults-2(adults a1 > 1?) --> received-0
  received-1(received) --> all-equal
  received-0(received) --> all-satisfy-0
  all-equal(all-equal) --> and(and)
  all-satisfy-0(all-satisfy m=0) --> and
```

[AllActorTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/AllActorTile.soda
[ReceivedSigmaPTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/ReceivedSigmaPTile.soda
[AllSatisfyPTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/AllSatisfyPTile.soda
[AllEqualTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/AllEqualTile.soda
[AttributePTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/AttributePTile.soda
[FilterActorTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/FilterActorTile.soda
[AllAtLeastTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/AllAtLeastTile.soda
[SigmaTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/SigmaTile.soda
[CombineBooleanTile]: https://github.com/julianmendez/tiles/blob/master/core/src/main/scala/soda/tiles/fairness/tile/CombineBooleanTile.soda
[CcsSingleGuardianPipeline]: https://github.com/julianmendez/tiles/blob/master/examples/src/main/scala/soda/tiles/fairness/example/childcaresubsidy/CcsSingleGuardianPipeline.soda
[CcsNoSubsidyPipeline]: https://github.com/julianmendez/tiles/blob/master/examples/src/main/scala/soda/tiles/fairness/example/childcaresubsidy/CcsNoSubsidyPipeline.soda
[CcsPerChildPipeline]: https://github.com/julianmendez/tiles/blob/master/examples/src/main/scala/soda/tiles/fairness/example/childcaresubsidy/CcsPerChildPipeline.soda
[CcsPerFamilyPipeline]: https://github.com/julianmendez/tiles/blob/master/examples/src/main/scala/soda/tiles/fairness/example/childcaresubsidy/CcsPerFamilyPipeline.soda

<script>
  window.mermaid.init(undefined, document.querySelectorAll('.language-mermaid'));
</script>



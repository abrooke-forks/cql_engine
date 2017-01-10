library AggregateFunctions version '1'

context Population

//AllTrue
define AllTrueAllTrue               : AllTrue({true,true})
define AllTrueTrueFirst             : AllTrue({true,false})
define AllTrueFalseFirst            : AllTrue({false,true})
define AllTrueAllTrueFalseTrue      : AllTrue({true,false,true})
define AllTrueAllFalseTrueFalse     : AllTrue({false,true,false})
define AllTrueNullFirst             : AllTrue({null,true,true})
define AllTrueEmptyList             : AllTrue({})
define AllTrueAllNull               : AllTrue({null as Boolean, null as Boolean, null as Boolean})

//AnyTrue
define AnyTrueAllTrue               : AnyTrue({true,true})
define AnyTrueAllFalse              : AnyTrue({false,false})
define AnyTrueAllTrueFalseTrue      : AnyTrue({true,false,true})
define AnyTrueAllFalseTrueFalse     : AnyTrue({false,true,false})
define AnyTrueTrueFirst             : AnyTrue({true,false})
define AnyTrueFalseFirst            : AnyTrue({false,true})
define AnyTrueNullFirstThenTrue     : AnyTrue({null,true})
define AnyTrueNullFirstThenFalse    : AnyTrue({null,false})
define AnyTrueEmptyList             : AnyTrue({})
define AnyTrueAllNull               : AnyTrue({null as Boolean, null as Boolean, null as Boolean})

//Avg
define AvgTestDecimal: Avg({ 1.0, 2.0, 3.0, 6.0 })
define AvgTestQuantity: Avg({ 122.5 'g', 75.5 'g', 15.0 'g', 118.8 'g' })
define AvgTestNull: Avg({ null as Decimal })
define AvgTestNull2: Avg({ null as Quantity, null as Quantity })
define AvgTestDecimalWithNull: Avg({ 1.0, 2.0, null as Decimal, 3.0, 6.0 })
define AvgTestQuantityWithNull: Avg({ 122.5 'g', null as Quantity, 75.5 'g', 15.0 'g', null as Quantity, 118.8 'g' })

//Count
define CountTest1: Count({ 15, 5, 99, null, 1 })
define CountTestDateTime: Count({ DateTime(2014), DateTime(2001), DateTime(2010) })
define CountTestTime: Count({ @T15:59:59.999, @T05:59:59.999, @T20:59:59.999 })
define CountTestBoolean: Count ({ true, null, false, null, null, true, false, null })
define CountTestList: Count ({ {1,2,3}, {4, 5, 6}, null, {7,8,9} })
define CountTestAllNull: Count ({ null as Integer, null as Integer, null as Integer })

//Max
define MaxTestInteger: Max({ 5, 12, 1, 15, 0, 4, 90, 44 })
define MaxTestDecimal: Max({ 5.4, 12.2, null, 1.8, 15.0, null, 0.0, 4.25, 90.9, 44.4 })
define MaxTestString: Max({ 'hi', 'bye', 'zebra' })
define MaxTestDateTime: Max({ DateTime(2012, 10, 5), DateTime(2012, 9, 5), DateTime(2012, 10, 6) })
define MaxTestTime: Max({ @T15:59:59.999, @T05:59:59.999, @T20:59:59.999 })

//Median
define MedianTestDecimalEven: Median({6.0, 5.0, 4.0, 3.0, 2.0, 1.0})
define MedianTestQuantityEven: Median({6.0 'cm', null, 5.0 'cm', 4.0 'cm', null, 3.0 'cm', 2.0 'cm', 1.0 'cm'})
define MedianTestDecimalOdd: Median({5.0, 4.0, 3.0, 2.0, 1.0})
define MedianTestQuantityOdd: Median({null, 5.0 'cm', 4.0 'cm', null, 3.0 'cm', 2.0 'cm', 1.0 'cm'})
define MedianTestNull: Median({ null as Decimal, null as Decimal })

//Min
define MinTestInteger: Min({5, 12, 1, 15, 0, 4, 90, 44})
define MinTestDecimal: Min({ 5.4, 12.2, null, 1.8, 15.0, null, 0.0, 4.25, 90.9, 44.4 })
define MinTestString: Min({'hi', 'bye', 'zebra'})
define MinTestDateTime: Min({ DateTime(2012, 10, 5), DateTime(2012, 9, 5), DateTime(2012, 10, 6) })
define MinTestTime: Min({ @T15:59:59.999, @T05:59:59.999, @T20:59:59.999 })

//Mode
define ModeTestInteger: Mode({ 2, 1, null, 8, 2, 9, 1, 9, null, 9 })
define ModeTestDecimal: Mode({ 5.4, 12.2, null, 1.8, 15.0, null, 0.0, 4.25, 90.9, 44.4, 12.2 })
define ModeTestNull: Mode({ null, null, null })
define ModeTestDateTime: Mode({ DateTime(2012, 10, 5), DateTime(2012, 9, 5), DateTime(2012, 10, 6), DateTime(2012, 9, 5) })
define ModeTestTime: Mode({ @T15:59:59.999, @T05:59:59.999, @T20:59:59.999, @T05:59:59.999 })

//PopulationStdDev
define PopStdDevTestDecimal: PopulationStdDev({ 1.0, null, 2.0, 3.0, null, 4.0, 5.0 })
define PopStdDevTestQuantity: PopulationStdDev({ 1.0 'g', 2.0 'g', null, 3.0 'g', null, 4.0 'g', 5.0 'g' })

//PopulationVariance
define PopVarianceTestDecimal: PopulationVariance({ 1.0, 2.0, 3.0, 4.0, 5.0 })
define PopVarianceTestQuantity: PopulationVariance({ 1.0 'g', null, 2.0 'g', 3.0 'g', 4.0 'g', null, 5.0 'g' })

//StdDev
define StdDevTestDecimal: StdDev({ 1.0, 2.0, 3.0, 4.0, 5.0 })
define StdDevTestQuantity: StdDev({ null, 1.0 'mm', 2.0 'mm', 3.0 'mm', null, 4.0 'mm', 5.0 'mm' })

//Sum
define SumTest1: Sum({ 6.0, 2.0, 3.0, 4.0, 5.0 })
define SumTestNull: Sum({ null, 1, null })
define SumTestAllNull: Sum({ null as Integer, null as Integer, null as Integer })

//Variance
define VarianceTestDecimal: Variance({ null, 1.0, 2.0, 3.0, 4.0, 5.0, null })
define VarianceTestQuantity: Variance({ 1.0 'cm', null, 2.0 'cm', 3.0 'cm', null, 4.0 'cm', 5.0 'cm' })

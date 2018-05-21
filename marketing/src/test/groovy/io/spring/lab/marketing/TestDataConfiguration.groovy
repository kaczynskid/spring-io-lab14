package io.spring.lab.marketing

import io.spring.lab.marketing.special.SpecialRepository

class TestDataConfiguration {

    static void specialsTestData(SpecialRepository specials) {
        new TestDataInit(specials).run(null)
    }
}

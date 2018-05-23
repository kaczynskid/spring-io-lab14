package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        name 'calculate-regular-price'
        method 'POST'
        url '/specials/5/calculate'
        body([
                unitPrice: 30.0,
                unitCount: 3
        ])
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
    response {
        status 200
        body([
                totalPrice: 90.0
        ])
        headers {
            header('Content-Type', 'application/json;charset=UTF-8')
        }
    }
}

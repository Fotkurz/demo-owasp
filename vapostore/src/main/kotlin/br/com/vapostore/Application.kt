package br.com.vapostore

import io.micronaut.runtime.Micronaut.build

fun main(args: Array<String>) {
	build()
		.args(*args)
		.packages("br.com.vapostore")
		.start()
}


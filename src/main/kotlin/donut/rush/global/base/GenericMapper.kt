package donut.rush.global.base

interface GenericMapper<D, E> {
    fun toDomain(entity: E): D

    fun toEntity(domain: D): E
}

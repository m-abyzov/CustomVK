# Домашнее задание по лекции "Async + RxJava"

## Реализовать асинхронную загрузку постов

1. Данные из JSON начинаем брать асинхронно, реактивно с помощью [RxJava2](https://github.com/ReactiveX/RxJava/tree/2.x).

    ⚠️ Не забываем отписываться при закрытии экрана

2. Добавить обработку ошибок на списки постов. Показывать диалог с описанием ошибки:

    |Загловок|Текст|Кнопка|
    |-|-|-|
    |Ошибка|При обработке запроса произошла ошибка|OK|

3. `DiffUtil` теперь считаем на фоновом потоке.

#### Дополнительное задание

1. Добавить заглушки, которые будут отображаться во время загрузки постов (можно использовать [shimmer-android](https://github.com/facebook/shimmer-android))

Например:

![Shimmers](./shimmers.jpg)

_Можно добавить задержку перед загрузкой постов._

2. Добавить заглушки для ошибок загрузки (в случае, если данные загружаем первый раз).

Например:

![Error placeholder](./error-placeholder.jpg)

_P.S. Можно добавить случайную ошибку при загрузке, например:_

```kotlin
Single.fromCallable {
    if (Ranom.nextInt(10) == 0) {
        throw RuntimeException(":(")
    }
    readPostsFromJson()
}
```
let itemApi = Vue.resource('/item/{id}')
let itemFilterApi = Vue.resource('/items/selected')
let tagApi = Vue.resource('/tags')
let cartApi = Vue.resource('/cart')
let cartRecoveryApi = Vue.resource('/cart/recovery')

function getIndex(list, id) {
    for (let i = 0; i < list.length; i++) {
        if (list[i].id === id) {
            return i;
        }
    }
    return -1;
}

Vue.component('cart-header', {
    template:
        '<thead>' +
        '<tr>' +
        '<th>Name</th>' +
        '<th>Description</th>' +
        '<th>Tags</th>' +
        '<th>Remove</th>' +
        '</tr>' +
        '</thead>'
});

Vue.component('cart-row', {
    props: ['item', 'cart'],
    template:
        '<tbody>' +
        '<tr>' +
        '<td>{{ item.name }}</td>' +
        '<td>{{ item.description }}</td>' +
        '<td>{{ item.tags.join() }}</td>' +
        '<td><input type="button" value="Remove" @click="deleteFromCart"/></td>' +
        '</tr>' +
        '</tbody>',
    methods: {
        deleteFromCart: function () {
            cartApi.delete({}, this.item.id).then(result =>
                result.json().then(data => {
                    if (data) {
                        let index = getIndex(this.cart, this.item.id);
                        this.cart.splice(index, 1);
                    }
                })
            )
        }
    }
});

Vue.component('cart-table', {
    props: ['cart'],
    template:
        '<table>' +
        '<cart-header/>' +
        '<cart-row v-for="item in cart" :key="item.id" :item="item" :cart="cart"/>' +
        '</table>',
    created: function () {
        cartRecoveryApi.get().then(result =>
            result.json().then(data => {
                    if (data) {
                        data.forEach(item => this.cart.push(item))
                    }
                }
            )
        )
    }
});

Vue.component('table-header', {
    template:
        '<thead>' +
        '<tr>' +
        '<th>Name</th>' +
        '<th>Description</th>' +
        '<th>Tags</th>' +
        '<th>Add</th>' +
        '</tr>' +
        '</thead>'
});

Vue.component('table-row', {
    props: ['item', 'cart'],
    template:
        '<tbody>' +
        '<tr>' +
        '<td>{{ item.name }}</td>' +
        '<td>{{ item.description }}</td>' +
        '<td>{{ item.tags.join() }}</td>' +
        '<td><input type="button" value="Add" @click="addToCart"/></td>' +
        '</tr>' +
        '</tbody>',
    methods: {
        addToCart: function () {
            cartApi.update({}, this.item.id).then(result =>
                result.json().then(data => {
                    if (data) {
                        this.cart.push(this.item);
                    }
                })
            )
        }
    }
});

Vue.component('item-table', {
    props: ['items', 'cart'],
    template:
        '<table>' +
        '<table-header/>' +
        '<table-row v-for="item in items" :key="item.id" :item="item" :cart="cart"/>' +
        '</table>'
});

Vue.component('item-filter', {
    props: ['items', 'tags'],
    data: function () {
        return {
            name: '',
            description: '',
            selectedTags: []
        }
    },
    template:
        '<div>' +
        '<input type="text" placeholder="Name" v-model="name"> <br>' +
        '<textarea cols="50" placeholder="Description" v-model="description"></textarea> <br>' +
        '<select multiple v-model="selectedTags"><option v-for="tag in tags">{{ tag }}</option></select> <br>' +
        '<input type="button" value="Filter" @click="filter"/>' +
        '</div>',
    created: function () {
        tagApi.get().then(result =>
            result.json().then(data =>
                data.forEach(tag => this.tags.push(tag))
            )
        )
    },
    methods: {
        filter: function () {
            let item = {
                name: this.name,
                description: this.description,
                tags: this.selectedTags
            }
            itemFilterApi.save({}, item).then(result =>
                result.json().then(data => {
                    this.items.splice(0, this.items.length);
                    data.forEach(item => this.items.push(item))
                })
            )
        }
    }
})

Vue.component('shop-component', {
    props: ['items', 'tags', 'cart'],
    template: '<div>' +
        '<h2>Item filter</h2>' +
        '<item-filter :items="items" :tags="tags"/>' +
        '<h2>Shop</h2>' +
        '<item-table :items="items" :cart="cart"/>' +
        '<h2>Your Cart</h2>' +
        '<cart-table :items="items" :cart="cart"/>' +
        '<input type="button" value="Pay" @click="payForCart"/>' +
        '</div>',
    created: function () {
        itemApi.get().then(result =>
            result.json().then(data =>
                data.forEach(item => this.items.push(item))
            )
        )
    },
    methods: {
        payForCart: function () {
            cartApi.get().then(result =>
                result.json().then(data => {
                    if (data) {
                        this.cart.splice(0, this.cart.length);
                    }
                })
            )
        }
    }
});

let app = new Vue({
    el: '#app',
    template: '<shop-component :items="items" :cart="cart" :tags="tags"/>',
    data: {
        items: [],
        tags: [],
        cart: []
    }
});
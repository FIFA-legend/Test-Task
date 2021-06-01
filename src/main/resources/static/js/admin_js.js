let itemApi = Vue.resource('/item/{id}')
let itemForceApi = Vue.resource('/item/force/{id}')
let itemFilterApi = Vue.resource('/items/selected')
let tagApi = Vue.resource('/tags')

function getIndex(list, id) {
    for (let i = 0; i < list.length; i++) {
        if (list[i].id === id) {
            return i;
        }
    }
    return -1;
}

Vue.component('item-form', {
    props: ['items', 'tags', 'itemToEdit', 'error'],
    data: function () {
        return {
            id: -1,
            name: '',
            description: '',
            selectedTags: []
        }
    },
    watch: {
        itemToEdit: function (newValue, oldValue) {
            this.id = newValue.id;
            this.name = newValue.name;
            this.description = newValue.description;
            this.selectedTags = newValue.selectedTags;
        }
    },
    template:
        '<div>' +
        '<input type="text" placeholder="Name" v-model="name"> <br>' +
        '<textarea cols="50" placeholder="Description" v-model="description"></textarea> <br>' +
        '<select multiple v-model="selectedTags"><option v-for="tag in tags">{{ tag }}</option></select> <br>' +
        '<input type="button" value="Save" @click="save"/>' +
        '<input type="button" value="Force Update" @click="forceUpdate"/>' +
        '<p style="color: red;">{{ error }}</p>' +
        '</div>',
    methods: {
        save: function () {
            let item = {
                name: this.name,
                description: this.description,
                tags: this.selectedTags
            }
            if (this.id) {
                itemApi.update({id: this.id}, item).then(result => {
                    if (result.body) {
                        result.json().then(data => {
                                let index = getIndex(this.items, data.id);
                                this.items.splice(index, 1, data);
                                this.error = '';
                            }
                        )
                    } else {
                        this.error = 'This item is in cart';
                    }
                })
            } else {
                itemApi.save({}, item).then(result =>
                    result.json().then(data => {
                        this.items.push(data);
                        this.name = '';
                        this.description = '';
                        this.selectedTags = [];
                    })
                )
            }
        },
        forceUpdate: function () {
            let item = {
                name: this.name,
                description: this.description,
                tags: this.selectedTags
            }
            if (this.id) {
                itemForceApi.update({id: this.id}, item).then(result =>
                    result.json().then(data => {
                        let index = getIndex(this.items, data.id);
                        this.items.splice(index, 1, data);
                        this.error = '';
                    })
                )
            }
        }
    },
    created: function () {
        tagApi.get().then(result =>
            result.json().then(data =>
                data.forEach(tag => this.tags.push(tag))
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
        '<th>Update</th>' +
        '<th>Delete</th>' +
        '</tr>' +
        '</thead>'
});

Vue.component('table-row', {
    props: ['item', 'editItem', 'items'],
    template:
        '<tbody>' +
        '<tr>' +
        '<td>{{ item.name }}</td>' +
        '<td>{{ item.description }}</td>' +
        '<td>{{ item.tags.join() }}</td>' +
        '<td><input type="button" value="Edit" @click="edit"/></td>' +
        '<td><input type="button" value="Delete" @click="deleteItem"></td>' +
        '</tr>' +
        '</tbody>',
    methods: {
        edit: function () {
            this.editItem(this.item);
        },
        deleteItem: function () {
            itemApi.delete({id: this.item.id}).then(result =>
                result.json().then(data => {
                    if (data) {
                        let index = getIndex(this.items, this.item.id);
                        this.items.splice(index, 1);
                    }
                })
            )
        }
    }
});

Vue.component('item-table', {
    props: ['items', 'editItem'],
    template:
        '<table>' +
        '<table-header/>' +
        '<table-row v-for="item in items" :key="item.id" :item="item" :items="items" :editItem="editItem"/>' +
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

Vue.component('item-list', {
    props: ['items', 'tags', 'error'],
    data: function () {
        return {
            item: null
        }
    },
    template: '<div>' +
        '<h2>Item addition</h2>' +
        '<item-form :items="items" :tags="tags" :itemToEdit="item" :error="error"></item-form> <br>' +
        '<item-table :items="items" :editItem="editItem"/>' +
        '<h2>Item filter</h2>' +
        '<item-filter :items="items" :tags="tags"/>' +
        '</div>',
    created: function () {
        itemApi.get().then(result =>
            result.json().then(data =>
                data.forEach(item => this.items.push(item))
            )
        )
    },
    methods: {
        editItem: function (item) {
            this.item = item;
        }
    }
});

let app = new Vue({
    el: '#app',
    template: '<item-list :items="items" :tags="tags" :error="error"/>',
    data: {
        error: '',
        items: [],
        tags: []
    }
});
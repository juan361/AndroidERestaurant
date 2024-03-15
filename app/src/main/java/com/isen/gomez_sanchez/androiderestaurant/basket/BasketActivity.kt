package com.isen.gomez_sanchez.androiderestaurant.basket

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.isen.gomez_sanchez.androiderestaurant.HomeActivity
import com.isen.gomez_sanchez.androiderestaurant.R

class BasketActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasketView()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun BasketView() {
    val context = LocalContext.current
    val basketItems = remember {
        mutableStateListOf<BasketItem>()
    }
    Column {
        TopAppBar({
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                IconButton(
                    onClick = {
                        val intent = Intent(context, HomeActivity::class.java)
                        context.startActivity(intent)

                    },
                    modifier = Modifier.background(Color.Transparent)
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_launcher),
                        contentDescription = null,
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Panier")
            }

        })
        LazyColumn {
            items(basketItems) {
                BasketItemView(it,basketItems)
            }
        }
    }
    basketItems.addAll(Basket.current(context).items)
}


@Composable fun BasketItemView(item: BasketItem, basketItems: MutableList<BasketItem>) {
    Card {
        val context = LocalContext.current
        Card(
            border = BorderStroke(1.dp, Color.Black),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Row(Modifier.padding(8.dp)) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.dish.images.first())
                        .build(),
                    null,
                    placeholder = painterResource(R.drawable.ic_launcher_foreground),
                    error = painterResource(R.drawable.ic_launcher_foreground),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .clip(RoundedCornerShape(10))
                        .padding(8.dp)
                )
                Column {

                    Text(text = item.dish.name)
                    Text(text = "${item.dish.prices.first().price} €")
                }

                Spacer(Modifier.weight(1f))
                Text(
                    text = item.count.toString(),
                    modifier = Modifier.align(alignment = Alignment.CenterVertically)
                )
                Button(onClick = {
                    // delete item and redraw view
                    Basket.current(context).delete(item, context)
                    basketItems.clear()
                    basketItems.addAll(Basket.current(context).items)
                }) {
                    Text(text = "X")
                }
            }
        }
    }
}

